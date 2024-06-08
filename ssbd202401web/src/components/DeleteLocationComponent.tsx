import { useTranslation } from "react-i18next";
import { useLocations } from "../hooks/useLocations.ts";
import { Box, Button, Typography } from "@mui/material";
import DeleteIcon from "@mui/icons-material/Delete";
import { useState } from "react";
import ConfirmChangeModal from "./ConfirmChangeModal.tsx";
import { useNavigate } from "react-router-dom";
import { Pathnames } from "../router/Pathnames.ts";

export default function DeleteLocationButton({
  locationId,
  fetchLocation,
}: {
  locationId: string;
  fetchLocation: () => void;
}) {
  const { t } = useTranslation();
  const { deleteLocationById } = useLocations();
  const [open, setOpen] = useState(false);
  const navigate = useNavigate();

  const handleDelete = async () => {
    try {
      await deleteLocationById(locationId);
      fetchLocation();
      setOpen(false);
      navigate(Pathnames.manager.locations);
    } catch (error) {
      console.error("Error deleting location:", error);
    }
  };

  return (
    <>
      <Box
        sx={{
          display: "flex",
          flexDirection: "column",
          alignItems: "start",
          marginY: 4,
          marginX: 8,
        }}
      >
        <Typography variant="h4">{t("deleteLocation")}</Typography>
        <Typography variant="body1">{t("clickButToDelLocation")}</Typography>
        <Button
          variant="contained"
          color="secondary"
          onClick={() => setOpen(true)}
          startIcon={<DeleteIcon />}
          sx={{ marginY: 2 }}
        >
          {t("deleteLocation")}
        </Button>
      </Box>
      <ConfirmChangeModal
        callback={handleDelete}
        handleClose={() => setOpen(false)}
        open={open}
      />
    </>
  );
}
