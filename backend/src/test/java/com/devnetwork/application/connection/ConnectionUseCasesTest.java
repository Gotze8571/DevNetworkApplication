package com.devnetwork.application.connection;

import com.devnetwork.application.member.MemberSummaries;
import com.devnetwork.domain.connection.ConnectionActionNotAllowedException;
import com.devnetwork.domain.connection.ConnectionAlreadyExistsException;
import com.devnetwork.domain.connection.ConnectionNotFoundException;
import com.devnetwork.domain.user.User;
import com.devnetwork.support.InMemoryConnectionRepository;
import com.devnetwork.support.InMemoryProfileRepository;
import com.devnetwork.support.InMemoryUserRepository;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ConnectionUseCasesTest {

    private final InMemoryUserRepository users = new InMemoryUserRepository();
    private final InMemoryConnectionRepository connections = new InMemoryConnectionRepository();
    private final MemberSummaries summaries = new MemberSummaries(users, new InMemoryProfileRepository());

    private final RequestConnectionUseCase request = new RequestConnectionUseCase(connections, users, summaries);
    private final AcceptConnectionUseCase accept = new AcceptConnectionUseCase(connections, summaries);
    private final RemoveConnectionUseCase remove = new RemoveConnectionUseCase(connections);
    private final ListConnectionsUseCase list = new ListConnectionsUseCase(connections, summaries);

    private final UUID ada = user("ada@example.com", "Ada");
    private final UUID grace = user("grace@example.com", "Grace");
    private final UUID linus = user("linus@example.com", "Linus");

    @Test
    void requestShowsAsOutgoingForSenderAndIncomingForRecipient() {
        ConnectionView sent = request.execute(ada, grace);

        assertThat(sent.state()).isEqualTo(ConnectionView.State.OUTGOING);
        assertThat(sent.member().displayName()).isEqualTo("Grace");
        assertThat(list.execute(grace)).singleElement()
                .satisfies(view -> {
                    assertThat(view.state()).isEqualTo(ConnectionView.State.INCOMING);
                    assertThat(view.member().displayName()).isEqualTo("Ada");
                });
    }

    @Test
    void recipientCanAcceptButSenderCannot() {
        ConnectionView sent = request.execute(ada, grace);

        assertThatThrownBy(() -> accept.execute(ada, sent.id())).isInstanceOf(ConnectionActionNotAllowedException.class);
        assertThat(accept.execute(grace, sent.id()).state()).isEqualTo(ConnectionView.State.CONNECTED);
        assertThat(list.execute(ada)).singleElement()
                .extracting(ConnectionView::state).isEqualTo(ConnectionView.State.CONNECTED);
    }

    @Test
    void requestingSomeoneWhoAlreadyAskedYouAcceptsTheirRequest() {
        request.execute(ada, grace);

        assertThat(request.execute(grace, ada).state()).isEqualTo(ConnectionView.State.CONNECTED);
    }

    @Test
    void duplicateRequestIsRejected() {
        request.execute(ada, grace);

        assertThatThrownBy(() -> request.execute(ada, grace)).isInstanceOf(ConnectionAlreadyExistsException.class);
    }

    @Test
    void cannotConnectWithYourself() {
        assertThatThrownBy(() -> request.execute(ada, ada)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void outsiderCannotSeeAcceptOrRemoveSomeoneElsesConnection() {
        ConnectionView sent = request.execute(ada, grace);

        assertThat(list.execute(linus)).isEmpty();
        assertThatThrownBy(() -> accept.execute(linus, sent.id())).isInstanceOf(ConnectionNotFoundException.class);
        assertThatThrownBy(() -> remove.execute(linus, sent.id())).isInstanceOf(ConnectionNotFoundException.class);
    }

    @Test
    void eitherSideCanRemoveAndThenRequestAgain() {
        ConnectionView sent = request.execute(ada, grace);
        remove.execute(grace, sent.id());

        assertThat(list.execute(ada)).isEmpty();
        assertThat(request.execute(ada, grace).state()).isEqualTo(ConnectionView.State.OUTGOING);
    }

    private UUID user(String email, String name) {
        return users.save(User.register(email, name, "hash")).getId();
    }
}
