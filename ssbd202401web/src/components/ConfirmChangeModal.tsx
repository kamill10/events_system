import { Dialog, DialogTitle, DialogContent, DialogContentText, DialogActions, Button } from "@mui/material"

export default function ConfirmChangeModal({
  open,
  handleClose,
  callback,
} : {
  open: boolean,
  handleClose: () => void,
  callback: () => void
}) {
  return (
    <Dialog
        open={open}
        onClose={handleClose}
        aria-labelledby="alert-dialog-title"
        aria-describedby="alert-dialog-description"
      >
        <DialogTitle id="alert-dialog-title">{"Confirm change"}</DialogTitle>
        <DialogContent>
          <DialogContentText id="alert-dialog-description">
            Are you sure you want to procceed?
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button 
            onClick={() => {
              callback();
              handleClose();
            }}
          >Yes</Button>
          <Button onClick={handleClose} autoFocus color="error">
            No
          </Button>
        </DialogActions>
      </Dialog>
  )
}