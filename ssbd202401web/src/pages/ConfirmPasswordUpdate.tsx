import { Box, Button, Typography } from "@mui/material";
import { useState } from "react";
import { Link, useSearchParams } from "react-router-dom";
import { useAccount } from "../hooks/useAccount";

export default function ConfirmPasswordUpdate() {
  const [searchParams] = useSearchParams();
  const { confirmPasswordUpdate } = useAccount();
  const [failed, setFailed] = useState(false);
  const [pressed, setPressed] = useState(false);

  async function verify() {
    setPressed(true);
    const key = searchParams.get("token");
    if (!key) {
      setFailed(true);
    } else {
      const err = await confirmPasswordUpdate(key);
      if (err) {
        setFailed(true);
      }
    }
  }

  if (failed) {
    return (
      <Box
        component={"div"}
        sx={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          height: "100vh",
        }}
      >
        <Box
          component={"div"}
          sx={{
            boxShadow: 6,
            padding: "3rem",
          }}
        >
          <Typography variant="h3" textAlign={"center"}>
            Something happened :((
          </Typography>
          <Typography variant="h6" textAlign={"center"}>
            There was an error while confirming your password change. If you
            think this is a mistake, contact support.
          </Typography>
          <Typography variant="h6" textAlign={"center"}>
            <Link to={"/"}>Go to home page</Link>
          </Typography>
        </Box>
      </Box>
    );
  }

  if (!pressed) {
    return (
      <Box
        component={"div"}
        sx={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          height: "100vh",
        }}
      >
        <Box
          component={"div"}
          sx={{
            boxShadow: 6,
            padding: "3rem",
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
          }}
        >
          <Typography variant="h3" textAlign={"center"}>
            Confirm your password change!
          </Typography>
          <Typography variant="h6" textAlign={"center"}>
            Click the button below to confirm your password change!
          </Typography>
          <Button
            onClick={verify}
            variant="contained"
            sx={{
              mt: 3,
              mb: 2,
            }}
          >
            Verify
          </Button>
          <Typography variant="h6" textAlign={"center"}>
            <Link to={"/"}>Go to home page</Link>
          </Typography>
        </Box>
      </Box>
    );
  }

  return (
    <Box
      component={"div"}
      sx={{
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        height: "100vh",
      }}
    >
      <Box
        component={"div"}
        sx={{
          boxShadow: 6,
          padding: "3rem",
        }}
      >
        <Typography variant="h3" textAlign={"center"}>
          Password has been changed!
        </Typography>
        <Typography variant="h6" textAlign={"center"}>
          You can go to login page manually, or&#x20;
          <Link to={"/login"}>click here!</Link>
        </Typography>
      </Box>
    </Box>
  );
}
