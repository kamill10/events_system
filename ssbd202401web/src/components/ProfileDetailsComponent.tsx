import { Box, Divider, Typography } from "@mui/material";
import { useAccount } from "../hooks/useAccount";
import { useEffect } from "react";
import ElementComponent from "./ElementComponent";

export default function ProfileDetailsComponent() {
  const { account, getMyAccount } = useAccount();

  useEffect(() => {
    getMyAccount();
  }, []);

  return (
    <Box component={Box} margin={"3rem"}>
      <Typography variant="h4">Personal data</Typography>
      <Divider
        sx={{
          marginTop: "3rem",
          marginBottom: "3rem",
        }}
      ></Divider>
      <ElementComponent
        label="Username"
        value={account?.username}
      />
      <ElementComponent
        label="First name"
        value={account?.firstName}
      />
      <ElementComponent
        label="Last name"
        value={account?.lastName}
      />
      <ElementComponent
        label="E-mail"
        value={account?.email}
      />
      <ElementComponent
        label="Gender"
        value={account?.gender}
      />
      <ElementComponent
        label="Gender"
        value={account?.active}
      />
      <ElementComponent
        label="Is active"
        value={account?.active ? "Yes" : "No"}
        color={account?.active ? "green" : "red"}
      />
      <ElementComponent
        label="Is verified"
        value={account?.verified ? "Yes" : "No"}
        color={account?.verified ? "green" : "red"}
      />
      <ElementComponent
        label="Is unlocked"
        value={account?.nonLocked ? "Yes" : "No"}
        color={account?.nonLocked ? "green" : "red"}
      />
      <ElementComponent
        label="Language preference"
        value={account?.language}
      />
    </Box>
  );
}
