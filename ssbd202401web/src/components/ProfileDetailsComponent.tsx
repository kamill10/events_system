import { Box, Divider, Typography } from "@mui/material";
import { useAccount } from "../hooks/useAccount";
import { useEffect } from "react";

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
      <Box
        sx={{
          marginBottom: "3rem",
        }}
      >
        <Typography variant="h6">Username</Typography>
        <Typography variant="subtitle1">{account?.username}</Typography>
      </Box>
      <Box
        sx={{
          marginBottom: "3rem",
        }}
      >
        <Typography variant="h6">First name</Typography>
        <Typography variant="subtitle1">{account?.firstName}</Typography>
      </Box>
      <Box
        sx={{
          marginBottom: "3rem",
        }}
      >
        <Typography variant="h6">Last name</Typography>
        <Typography variant="subtitle1">{account?.lastName}</Typography>
      </Box>
      <Box
        sx={{
          marginBottom: "3rem",
        }}
      >
        <Typography variant="h6">E-mail</Typography>
        <Typography variant="subtitle1">{account?.email}</Typography>
      </Box>
      <Box
        sx={{
          marginBottom: "3rem",
        }}
      >
        <Typography variant="h6">Gender</Typography>
        <Typography variant="subtitle1">{account?.gender}</Typography>
      </Box>
    </Box>
  );
}
