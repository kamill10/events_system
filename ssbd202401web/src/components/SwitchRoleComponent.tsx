import { Box, Typography, Switch } from "@mui/material";
import { t } from "i18next";
import { useAccount } from "../hooks/useAccount";
import { api } from "../axios/axios.config";
import { Pathnames } from "../router/Pathnames";
import { useNavigate } from "react-router-dom";
import { colors } from "../themes/themes";
import { useWindowWidth } from "../hooks/useWindowWidth";
import AdminPanelSettingsIcon from "@mui/icons-material/AdminPanelSettings";
import DescriptionIcon from "@mui/icons-material/Description";

export default function SwitchRoleComponent() {
  const width = useWindowWidth();
  const navigate = useNavigate();
  const { adminLayout, setAdminLayout } = useAccount();
  const handleSwitchClick = () => {
    // We check for layout before switching cause hook might not resolve right after click
    // Therefore we may end with previous state and sent request with wrong role
    if (!adminLayout) {
      api.switchActiveRoleToAdmin();
    } else {
      api.switchActiveRoleToManager();
    }
    setAdminLayout(!adminLayout);
    navigate(Pathnames.public.home);
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
          t("ROLE_MANAGER")
        )}
      </Typography>
    </Box>
  );
}
