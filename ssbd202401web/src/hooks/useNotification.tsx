import {  useSnackbar } from "notistack"
import { useEffect, useState } from "react";
import { NotificationType } from "../types/NotificationType";
import { Fragment } from "react/jsx-runtime";
import { IconButton } from "@mui/material";
import CloseIcon from '@mui/icons-material/Close';

const useNotification = () => {
    const [notification, setNotification] = useState<NotificationType | null>(null);
    const {enqueueSnackbar, closeSnackbar} = useSnackbar();
    const action = () => (
        <Fragment>
            <IconButton onClick={() => { closeSnackbar() }}>
                <CloseIcon/>
            </IconButton>
        </Fragment>
    );
    useEffect(()=>{
        console.log(notification);
        console.log("gragas");
        if(notification){
            enqueueSnackbar(notification.description, {
                variant: notification.type,
                autoHideDuration: 5000,
                action
            });
        }
    },[notification]);
    return setNotification;
}

export default useNotification;