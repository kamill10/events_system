import { createTheme } from "@mui/material";

const colors = {
  public: {
    primary: "#1C1678",
    secondary: "#8576FF",
  },
  manager: {
    primary: "#4D869C",
    secondary: "#7AB2B2",
  },
  admin: {
    primary: "#153448",
    secondary: "#3C5B6F",
  },
  participant: {
    primary: "#A34343",
    secondary: "#E9C874",
  },
};


export const ParticipantTheme = createTheme({
  palette: {
    primary: {
      main: colors.participant.primary,
    },
    secondary: {
      main: colors.participant.secondary,
    },
  },
});

export const ParticipantDarkTheme = createTheme({
    palette: {
        mode: "dark",
        primary: {
          main: colors.participant.primary,
        },
        secondary: {
          main: colors.participant.secondary,
        },
    },
});

export const AdminTheme = createTheme({
  palette: {
    primary: {
      main: colors.admin.primary,
    },
    secondary: {
      main: colors.admin.secondary,
    },
  },
});

export const AdminDarkTheme = createTheme({
    palette: {
        mode: "dark",
        primary: {
          main: colors.admin.secondary,
        },
        secondary: {
          main: colors.admin.primary,
        },
    },
});

export const ManagerTheme = createTheme({
  palette: {
    primary: {
      main: colors.manager.primary,
    },
    secondary: {
      main: colors.manager.secondary,
    },
  },
});

export const ManagerDarkTheme = createTheme({
    palette: {
        mode: "dark",
        primary: {
          main: colors.manager.primary,
        },
        secondary: {
          main: colors.manager.secondary,
        },
    },
    
});

export const PublicTheme = createTheme({
  palette: {
    primary: {
      main: colors.public.primary,
    },
    secondary: {
      main: colors.public.secondary,
    },
  },
});

export const PublicDarkTheme = createTheme({
    palette: {
        mode: "dark",
        primary: {
          main: colors.public.secondary,
        },
        secondary: {
          main: colors.public.primary,
        },
    },
});
