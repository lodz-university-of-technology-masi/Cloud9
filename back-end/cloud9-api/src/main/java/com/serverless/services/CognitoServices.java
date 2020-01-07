package com.serverless.services;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.*;
import com.serverless.handlers.user.ListUsersHandler;
import com.serverless.settings.CognitoSettings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.serverless.db.model.User;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class CognitoServices {
    private static final Logger LOG = LogManager.getLogger(CognitoServices.class);
    private AWSCognitoIdentityProvider cognito = AWSCognitoIdentityProviderClientBuilder.defaultClient();

    private boolean isRegularUser(UserType userType) {
        AdminListGroupsForUserResult result = cognito.adminListGroupsForUser(
                new AdminListGroupsForUserRequest()
                        .withUserPoolId(CognitoSettings.USER_POOL_ID)
                        .withUsername(userType.getUsername())
        );

        return result.getGroups().stream().noneMatch(groupType -> groupType.getGroupName().equals(CognitoSettings.USER_GROUP_NAME));
    }
    public List<User> listUser() {
        LOG.info("Call UserListFromCognito::listUser");
        ListUsersResult users = cognito.listUsers(new ListUsersRequest().withUserPoolId(CognitoSettings.USER_POOL_ID));

        return users.getUsers().stream()
                .filter(this::isRegularUser)
                .map(User::fromUserType)
                .collect(Collectors.toList());
    }

    public User getUser(String userId) {
        ListUsersResult users = cognito.listUsers(new ListUsersRequest().withUserPoolId(CognitoSettings.USER_POOL_ID));
        User user = users.getUsers().stream()
                .filter(userType -> Objects.equals(User.getSub(userType), userId))
                .findAny()
                .map(User::fromUserType)
                .orElse(null);
        return user;
    }

    public List<User> listUser(List<String> usersId) {
        ListUsersResult users = cognito.listUsers(new ListUsersRequest().withUserPoolId(CognitoSettings.USER_POOL_ID));
        return users.getUsers().stream()
                .filter(this::isRegularUser)
                .filter(u -> usersId.contains(u.getUsername()))
                .map(User::fromUserType)
                .collect(Collectors.toList());
    }
}
