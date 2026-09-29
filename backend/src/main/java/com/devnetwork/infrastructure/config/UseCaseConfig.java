package com.devnetwork.infrastructure.config;

import com.devnetwork.application.account.GetAccountUseCase;
import com.devnetwork.application.account.UpdateAccountUseCase;
import com.devnetwork.application.auth.LoginUseCase;
import com.devnetwork.application.auth.PasswordHasher;
import com.devnetwork.application.connection.AcceptConnectionUseCase;
import com.devnetwork.application.connection.ListConnectionsUseCase;
import com.devnetwork.application.connection.RemoveConnectionUseCase;
import com.devnetwork.application.connection.RequestConnectionUseCase;
import com.devnetwork.application.member.GetMemberUseCase;
import com.devnetwork.application.member.ListMembersUseCase;
import com.devnetwork.application.member.MemberSummaries;
import com.devnetwork.application.user.GetUserUseCase;
import com.devnetwork.application.user.RegisterUserUseCase;
import com.devnetwork.domain.connection.ConnectionRepository;
import com.devnetwork.domain.profile.ProfileRepository;
import com.devnetwork.domain.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Registers use cases as Spring beans, so the application layer itself stays framework-free.
 */
@Configuration
public class UseCaseConfig {

    // Users and authentication

    @Bean
    RegisterUserUseCase registerUserUseCase(UserRepository userRepository, PasswordHasher passwordHasher) {
        return new RegisterUserUseCase(userRepository, passwordHasher);
    }

    @Bean
    LoginUseCase loginUseCase(UserRepository userRepository, PasswordHasher passwordHasher) {
        return new LoginUseCase(userRepository, passwordHasher);
    }

    @Bean
    GetUserUseCase getUserUseCase(UserRepository userRepository) {
        return new GetUserUseCase(userRepository);
    }

    // Accounts

    @Bean
    GetAccountUseCase getAccountUseCase(UserRepository userRepository, ProfileRepository profileRepository) {
        return new GetAccountUseCase(userRepository, profileRepository);
    }

    @Bean
    UpdateAccountUseCase updateAccountUseCase(UserRepository userRepository, ProfileRepository profileRepository) {
        return new UpdateAccountUseCase(userRepository, profileRepository);
    }

    // Members

    @Bean
    MemberSummaries memberSummaries(UserRepository userRepository, ProfileRepository profileRepository) {
        return new MemberSummaries(userRepository, profileRepository);
    }

    @Bean
    ListMembersUseCase listMembersUseCase(UserRepository userRepository, MemberSummaries memberSummaries) {
        return new ListMembersUseCase(userRepository, memberSummaries);
    }

    @Bean
    GetMemberUseCase getMemberUseCase(UserRepository userRepository, ProfileRepository profileRepository) {
        return new GetMemberUseCase(userRepository, profileRepository);
    }

    // Connections

    @Bean
    ListConnectionsUseCase listConnectionsUseCase(ConnectionRepository connectionRepository,
                                                  MemberSummaries memberSummaries) {
        return new ListConnectionsUseCase(connectionRepository, memberSummaries);
    }

    @Bean
    RequestConnectionUseCase requestConnectionUseCase(ConnectionRepository connectionRepository,
                                                      UserRepository userRepository,
                                                      MemberSummaries memberSummaries) {
        return new RequestConnectionUseCase(connectionRepository, userRepository, memberSummaries);
    }

    @Bean
    AcceptConnectionUseCase acceptConnectionUseCase(ConnectionRepository connectionRepository,
                                                    MemberSummaries memberSummaries) {
        return new AcceptConnectionUseCase(connectionRepository, memberSummaries);
    }

    @Bean
    RemoveConnectionUseCase removeConnectionUseCase(ConnectionRepository connectionRepository) {
        return new RemoveConnectionUseCase(connectionRepository);
    }
}
