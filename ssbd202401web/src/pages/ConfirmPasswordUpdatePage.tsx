import { Button, Typography } from "@mui/material";
import { useState } from "react";
import { Link, useSearchParams } from "react-router-dom";
import { useAccount } from "../hooks/useAccount";
import CenteredContainerComponent from "../components/CenterdContainerComponent";
import ContainerWithShadowComponent from "../components/ContainerWithShadowComponent";

export default function ConfirmPasswordUpdatePage() {
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
      <CenteredContainerComponent>
        <ContainerWithShadowComponent>
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
        </ContainerWithShadowComponent>
      </CenteredContainerComponent>
    );
  }

  if (!pressed) {
    return (
      <CenteredContainerComponent>
        <ContainerWithShadowComponent>
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
        </ContainerWithShadowComponent>
      </CenteredContainerComponent>
    );
  }

  return (
    <CenteredContainerComponent>
      <ContainerWithShadowComponent>
        <Typography variant="h3" textAlign={"center"}>
          Password has been changed!
        </Typography>
        <Typography variant="h6" textAlign={"center"}>
          You can go to login page manually, or&#x20;
          <Link to={"/login"}>click here!</Link>
        </Typography>
      </ContainerWithShadowComponent>
    </CenteredContainerComponent>
  );
}
