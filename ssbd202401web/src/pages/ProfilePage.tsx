import { Box, Divider, Paper, Tab, Tabs } from "@mui/material";
import { SyntheticEvent, useState } from "react";
import ProfileDetailsComponent from "../components/ProfileDetailsComponent";
import ChangeEmailComponent from "../components/ChangeEmailComponent";
import ChangePasswordComponent from "../components/ChangePasswordComponent";
import ChangePersonalDataComponent from "../components/ChangePersonalDataComponent";

export default function ProfilePage() {
  const [page, setPage] = useState(0);

  const handleChange = (_: SyntheticEvent, newValue: number) => {
    setPage(newValue);
  };

  return (
    <Box
      component={Paper}
      elevation={6}
      minHeight={"80vh"}
      maxHeight={"80vh"}
      overflow={"auto"}
    >
      <Tabs value={page} onChange={handleChange}>
        <Tab label="Profile details"></Tab>
        <Tab label="Change personal data"></Tab>
        <Tab label="Change password"></Tab>
        <Tab label="Change e-mail"></Tab>
      </Tabs>
      <Divider></Divider>
      {page == 0 && <ProfileDetailsComponent></ProfileDetailsComponent>}
      {page == 1 && <ChangePersonalDataComponent></ChangePersonalDataComponent>}
      {page == 2 && <ChangePasswordComponent></ChangePasswordComponent>}
      {page == 3 && <ChangeEmailComponent></ChangeEmailComponent>}
    </Box>
  );
}
