import {  useSnackbar } from "notistack";   
import { NotificationType } from "../types/NotificationType";
import { Fragment } from "react/jsx-runtime";
import { IconButton } from "@mui/material";
import CloseIcon from '@mui/icons-material/Close';

const useNotification = () => {
    const {enqueueSnackbar, closeSnackbar} = useSnackbar();
    const action = () => (
        <Fragment>
            <IconButton onClick={() => { closeSnackbar() }}>
                <CloseIcon/>
            </IconButton>
        </Fragment>
    );

    const sendNotification = (notification: NotificationType) => enqueueSnackbar(notification.description, {
        variant: notification.type,
        autoHideDuration: 5000,
        action
    });

    return sendNotification;
}

export default useNotification;