import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogContentText,
  DialogActions,
  Button,
} from "@mui/material";
import { useTranslation } from "react-i18next";

export default function ConfirmChangeModal({
  open,
  handleClose,
  callback,
}: {
  open: boolean;
  handleClose: () => void;
  callback: () => void;
}) {
  const { t } = useTranslation();
  return (
    <Dialog
      open={open}
      onClose={handleClose}
      aria-labelledby="alert-dialog-title"
      aria-describedby="alert-dialog-description"
    >
      <DialogTitle id="alert-dialog-title">{t("confirmChange")}</DialogTitle>
      <DialogContent>
        <DialogContentText id="alert-dialog-description">
          {t("areYouSure")}
        </DialogContentText>
      </DialogContent>
      <DialogActions>
        <Button
          onClick={() => {
            callback();
            handleClose();
          }}
        >
          {t("yes")}
        </Button>
        <Button onClick={handleClose} autoFocus color="error">
          {t("no")}
        </Button>
      </DialogActions>
    </Dialog>
  );
}
