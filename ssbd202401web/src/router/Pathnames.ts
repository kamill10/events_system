export const Pathnames = {
  unauth: {
    login: "/login",
    signIn: "/signin",
    forgotPassword: "/login/forgot-password",
  },
  public: {
    home: "/",
    events: "/events",
    event: "/events/:id",
    verifyAccount: "/verify-account",
    resetPassword: "/login/reset-password",
    confirmPasswordUpdate: "/change-my-password",
    confirmEmailUpdate: "/confirm-email",
    confirmUnblockAccount: "/unblock-account",
  },
  auth: {
    profile: "/profile",
    logout: "/logout",
  },
  participant: {
    myEvents: "/profile/events",
  },
  manager: {
    locations: "/locations",
    location: "/location/:id",
    speakers: "/speakers",
    speaker: "/speakers/:id",
  },
  admin: {
    accounts: "/accounts",
    account: "/accounts/:username",
  },
};
