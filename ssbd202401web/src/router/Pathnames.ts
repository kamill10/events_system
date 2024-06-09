export const Pathnames = {
  singlePage: {
    verifyAccount: "/verify-account",
    resetPassword: "/login/reset-password",
    confirmPasswordUpdate: "/change-my-password",
    confirmEmailUpdate: "/confirm-email",
    confirmUnblockAccount: "/unblock-account",
  },
  unauth: {
    login: "/login",
    signIn: "/signin",
    forgotPassword: "/login/forgot-password",
  },
  public: {
    home: "/",
    events: "/events",
    event: "/events/:id",
  },
  auth: {
    profile: "/profile",
    logout: "/logout",
  },
  participant: {
    myTickets: "/profile/tickets",
    ticket: "/profile/ticket/:id",
  },
  manager: {
    locations: "/locations",
    location: "/locations/:id",
    speakers: "/speakers",
    speaker: "/speakers/:id",
    room: "/rooms/:id",
  },
  admin: {
    accounts: "/accounts",
    account: "/accounts/:username",
  },
};
