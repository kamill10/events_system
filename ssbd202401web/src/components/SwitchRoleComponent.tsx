import { Box, Typography, Switch } from "@mui/material";
import { t } from "i18next";
import { useAccount } from "../hooks/useAccount";
import { colors } from "../themes/themes";
import { useWindowWidth } from "../hooks/useWindowWidth";
import AdminPanelSettingsIcon from "@mui/icons-material/AdminPanelSettings";
import DescriptionIcon from "@mui/icons-material/Description";

export default function SwitchRoleComponent() {
  const width = useWindowWidth();
  const { switchRoleToAdmin, switchRoleToManager, adminLayout } = useAccount();
  const handleSwitchClick = () => {
    // We check for layout before switching cause hook might not resolve right after click
    // Therefore we may end with previous state and sent request with wrong role
    if (adminLayout) {
      switchRoleToManager();
    } else {
      switchRoleToAdmin();
    }
  };

  return (
    <Box sx={{ display: "flex", alignItems: "center", mr: 2 }}>
      <Typography
        sx={{ mr: 1, color: colors.manager.secondary, display: "flex" }}
      >
        {width < 1020 ? <DescriptionIcon></DescriptionIcon> : t("ROLE_MANAGER")}
      </Typography>
      <Switch
        color="secondary"
        onChange={handleSwitchClick}
        checked={adminLayout}
        sx={{ mr: 1 }}
      />
      <Typography
        sx={{ mr: 1, color: colors.admin.secondary, display: "flex" }}
      >
        {width < 1020 ? (
          <AdminPanelSettingsIcon></AdminPanelSettingsIcon>
        ) : (
          t("ROLE_ADMIN")
        )}
      </Typography>
    </Box>
  );
}
