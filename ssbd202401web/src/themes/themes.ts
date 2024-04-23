import { createTheme } from "@mui/material";

const colors = {
    public: "#5800c4",
    manager: "#001fa8",
    admin: "#000000",
    participant: "#ba0c00"
};

export const ParticipantTheme = createTheme({
    palette: {
        primary: {
            main: colors.participant
        }
    }
});

export const AdminTheme = createTheme({
    palette: {
        primary: {
            main: colors.admin
        }
    }
});

export const ManagerTheme = createTheme({
    palette: {
        primary: {
            main: colors.manager
        }
    }
});

export const PublicTheme = createTheme({
    palette: {
        primary: {
            main: colors.public
        }
    }
});