import { Divider, Tab, Tabs } from "@mui/material";
import { SyntheticEvent, useState } from "react";
import ProfileDetailsComponent from "../components/ProfileDetailsComponent";
import ChangeEmailComponent from "../components/ChangeEmailComponent";
import ChangePasswordComponent from "../components/ChangePasswordComponent";
import ChangePersonalDataComponent from "../components/ChangePersonalDataComponent";
import ContainerComponent from "../components/ContainerComponent";
import { useTranslation } from "react-i18next";

export default function ProfilePage() {
  const { t } = useTranslation();
  const [page, setPage] = useState(0);

  const handleChange = (_: SyntheticEvent, newValue: number) => {
    setPage(newValue);
  };

  return (
    <ContainerComponent>
      <Tabs value={page} onChange={handleChange} sx={{
        flexWrap: "wrap"
      }}>
        <Tab label={t("profileDetails")}></Tab>
        <Tab label={t("changeProfileDetails")}></Tab>
        <Tab label={t("changePassword")}></Tab>
        <Tab label={t("changeEmail")}></Tab>
      </Tabs>
      <Divider></Divider>
      {page == 0 && <ProfileDetailsComponent></ProfileDetailsComponent>}
      {page == 1 && <ChangePersonalDataComponent></ChangePersonalDataComponent>}
      {page == 2 && <ChangePasswordComponent></ChangePasswordComponent>}
      {page == 3 && <ChangeEmailComponent></ChangeEmailComponent>}
    </ContainerComponent>
  );
}
