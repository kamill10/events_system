import { Box, Divider, Typography } from "@mui/material";
import AudiotrackIcon from "@mui/icons-material/Audiotrack";
import { Link } from "react-router-dom";
import { Pathnames } from "../router/Pathnames";
import { useAccount } from "../hooks/useAccount";

export default function FooterComponent() {
  const { isAuthenticated, isAdmin, isManager, isParticipant } = useAccount();
  return (
    <>
      <Box
        sx={{
          width: "100%",
          display: "flex",
          flexDirection: "column",
          justifyContent: "center",
          alignItems: "center",
          marginBottom: 2,
        }}
      >
        <Divider
          sx={{
            marginY: 7,
            width: "85vw",
          }}
        ></Divider>
        <Typography variant="h4" display={"flex"}>
          <AudiotrackIcon
            sx={{
              fontSize: 50,
            }}
          ></AudiotrackIcon>
          EventSymphony
        </Typography>
        {!isAuthenticated && (
          <Box
            sx={{
              display: "flex",
              justifyContent: "space-between",
              alignItems: "center",
              margin: 2,
            }}
          >
            <Link
              style={{
                margin: 10,
                textDecoration: "none",
                textAlign: "center",
                color: "grey",
              }}
              to={Pathnames.public.home}
            >
              Home page
            </Link>
            <Link
              style={{
                margin: 10,
                textDecoration: "none",
                textAlign: "center",
                color: "grey",
              }}
              to={Pathnames.public.login}
            >
              Log in
            </Link>
            <Link
              style={{
                margin: 10,
                textDecoration: "none",
                textAlign: "center",
                color: "grey",
              }}
              to={Pathnames.public.signIn}
            >
              Sign in
            </Link>
            <Link
              style={{
                margin: 10,
                textDecoration: "none",
                textAlign: "center",
                color: "grey",
              }}
              to={Pathnames.public.events}
            >
              Events
            </Link>
          </Box>
        )}
        {isParticipant && (
          <Box
            sx={{
              display: "flex",
              justifyContent: "space-between",
              alignItems: "center",
              margin: 2,
            }}
          >
            <Link
              style={{
                margin: 10,
                textDecoration: "none",
                textAlign: "center",
                color: "grey",
              }}
              to={Pathnames.participant.home}
            >
              Home page
            </Link>
            <Link
              style={{
                margin: 10,
                textDecoration: "none",
                textAlign: "center",
                color: "grey",
              }}
              to={Pathnames.participant.myEvents}
            >
              My events
            </Link>
            <Link
              style={{
                margin: 10,
                textDecoration: "none",
                textAlign: "center",
                color: "grey",
              }}
              to={Pathnames.public.events}
            >
              Events
            </Link>
          </Box>
        )}
        {isAdmin && (
          <Box
            sx={{
              display: "flex",
              justifyContent: "space-between",
              alignItems: "center",
              margin: 2,
            }}
          >
            <Link
              style={{
                margin: 10,
                textDecoration: "none",
                textAlign: "center",
                color: "grey",
              }}
              to={Pathnames.admin.home}
            >
              Home page
            </Link>
            <Link
              style={{
                margin: 10,
                textDecoration: "none",
                textAlign: "center",
                color: "grey",
              }}
              to={Pathnames.admin.accounts}
            >
              Accounts
            </Link>
          </Box>
        )}
        {isManager && (
          <Box
            sx={{
              display: "flex",
              justifyContent: "space-between",
              alignItems: "center",
              margin: 2,
            }}
          >
            <Link
              style={{
                margin: 10,
                textDecoration: "none",
                textAlign: "center",
                color: "grey",
              }}
              to={Pathnames.manager.home}
            >
              Home page
            </Link>
            <Link
              style={{
                margin: 10,
                textDecoration: "none",
                textAlign: "center",
                color: "grey",
              }}
              to={Pathnames.manager.events}
            >
              Events
            </Link>
            <Link
              style={{
                margin: 10,
                textDecoration: "none",
                textAlign: "center",
                color: "grey",
              }}
              to={Pathnames.manager.locations}
            >
              Locations
            </Link>
            <Link
              style={{
                margin: 10,
                textDecoration: "none",
                textAlign: "center",
                color: "grey",
              }}
              to={Pathnames.manager.speakers}
            >
              Speakers
            </Link>
          </Box>
        )}
        <Typography margin={2} variant="body2">
          @2024 EventSymphony. All rights reserved
        </Typography>
      </Box>
    </>
  );
}
