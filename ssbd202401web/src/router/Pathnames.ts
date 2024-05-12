export const Pathnames = {
    public: {
        home: "/",
        login: "/login",
        signIn: "/signin",
        events: "/events",
        forgotPassword: "/login/forgot-password",
        verifyAccount: "/verify-account",
        resetPassword: "/login/reset-password"
    },
    participant: {
        home: "/",
        events: "/events",
        event: "/events/:id",
        profile: "/profile",
        myEvents: "/profile/events",
        logout: "/logout",
    },
    manager: {
        home: "/",
        events: "/events",
        event: "/events/:id",
        profile: "/profile",
        myEvents: "/profile/events",
        locations: "/locations",
        location: "/location/:id",
        speakers: "/speakers",
        speaker: "/speakers/:id",
        logout: "/logout"
    },
    admin: {
        home: "/",
        profile: "/profile",
        users: "/users",
        user: "/users/:id",
        logout: "/logout"
    }
}