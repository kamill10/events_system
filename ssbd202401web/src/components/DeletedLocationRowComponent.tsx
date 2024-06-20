import { Location } from "../types/Location.ts";
import { Button, TableCell, TableRow } from "@mui/material";
import RestoreIcon from "@mui/icons-material/Restore";
import ConfirmChangeModal from "./ConfirmChangeModal.tsx";
import { useState } from "react";
import { PaginationRequestParams } from "../types/PaginationRequestParams.ts";
import { useTranslation } from "react-i18next";

export default function DeletedLocationRowComponent({
  location,
  getDeletedLocation,
  restoreLocation,
  fetchLocations,
  paginationRequestParams,
}: {
  location: Location;
  getDeletedLocation: (id: string) => Promise<unknown>;
  restoreLocation: (id: string) => Promise<unknown>;
  fetchLocations: (data: PaginationRequestParams) => void;
  paginationRequestParams: PaginationRequestParams;
}) {
  const [open, setOpen] = useState(false);
  const { t } = useTranslation();

  const handleOpen = async () => {
    try {
      await getDeletedLocation(location.id);
    } finally {
      setOpen(true);
    }
  };

  const handleClose = () => {
    setOpen(false);
  };

  const handleRestore = async () => {
    try {
      await restoreLocation(location.id);
    } finally {
      handleClose();
      fetchLocations(paginationRequestParams);
    }
  };

  return (
    <TableRow>
      <TableCell>{location.id}</TableCell>
      <TableCell align="right">{location.name}</TableCell>
      <TableCell align="right">{location.city}</TableCell>
      <TableCell align="right">{location.country}</TableCell>
      <TableCell align="right">{location.street}</TableCell>
      <TableCell align="right">{location.buildingNumber}</TableCell>
      <TableCell align="right">{location.postalCode}</TableCell>
      <TableCell align="right">
        <Button
          variant="contained"
          startIcon={<RestoreIcon />}
          onClick={handleOpen}
        >
          {t("restore")}
        </Button>
      </TableCell>
      <ConfirmChangeModal
        callback={handleRestore}
        handleClose={() => setOpen(false)}
        open={open}
      />
    </TableRow>
  );
}
