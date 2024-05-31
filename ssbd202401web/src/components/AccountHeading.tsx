import { Box, Typography, Divider } from "@mui/material";
import { t } from "i18next";
import { useAccount } from "../hooks/useAccount";

export default function AccountHeading() {
  const { account } = useAccount();
  return (
    <Box
      sx={{
        marginX: 2,
      }}
    >
      <Typography variant="h6">
        {t("welcome") + ", " + account?.username}
      </Typography>
      {account?.roles.length === 2 ? (
        /*Nie wiem jak to naprawić, jak się komuś chce to zapraszam*/
        <Typography variant="body2">
          {t("roles")}:{" "}
          {account?.roles.reduce(
            (previous, current) =>
              /*@ts-ignore*/
              t(previous) + ", " + t(current),
          )}
        </Typography>
      ) : (
        <Typography variant="body2">
          {/*@ts-ignore*/}
          {t("roles")}: {t(account?.roles[0])}
        </Typography>
      )}

      <Divider sx={{ marginTop: 2 }}></Divider>
    </Box>
  );
}
