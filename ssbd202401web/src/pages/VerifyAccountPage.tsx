import { Box, Button, Typography } from "@mui/material";
import { useState } from "react";
import { Link, useSearchParams } from "react-router-dom";
import { useAccount } from "../hooks/useAccount";
import CenteredContainerComponent from "../components/CenterdContainerComponent";
import ContainerWithShadowComponent from "../components/ContainerWithShadowComponent";

export default function VerifyAccountPage() {
  const [searchParams] = useSearchParams();
  const { verifyAccount } = useAccount();
  const [failed, setFailed] = useState(false);
  const [pressed, setPressed] = useState(false);

  async function verify() {
    setPressed(true);
    const key = searchParams.get("token");
    if (!key) {
      setFailed(true);
    } else {
      const err = await verifyAccount(key);
      if (err) {
        setFailed(true);
      }
    }
  }

  if (failed) {
    return (
      <CenteredContainerComponent>
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
            There was an error while verifying your account. If you think this
            is a mistake, contact support.
          </Typography>
          <Typography variant="h6" textAlign={"center"}>
            <Link to={"/"}>Go to home page</Link>
          </Typography>
        </Box>
      </CenteredContainerComponent>
    );
  }

  if (!pressed) {
    return (
      <CenteredContainerComponent>
        <ContainerWithShadowComponent>
          <Typography variant="h3" textAlign={"center"}>
            Verify your account!
          </Typography>
          <Typography variant="h6" textAlign={"center"}>
            Click the button below to verify your account!
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
        </ContainerWithShadowComponent>
      </CenteredContainerComponent>
    );
  }

  return (
    <CenteredContainerComponent>
      <Box
        component={"div"}
        sx={{
          boxShadow: 6,
          padding: "3rem",
        }}
      >
        <Typography variant="h3" textAlign={"center"}>
          Account has been activated!
        </Typography>
        <Typography variant="h6" textAlign={"center"}>
          You can go to login page manually, or&#x20;
          <Link to={"/login"}>click here!</Link>
        </Typography>as
      </Box>
    </CenteredContainerComponent>
  );
}
