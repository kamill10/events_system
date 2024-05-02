import { useNavigate } from "react-router-dom"
import { useAccount } from "../hooks/useAccount"
import { Backdrop, CircularProgress } from "@mui/material";
import { useEffect } from "react";
import useNotification from "../hooks/useNotification";

export default function Logout() {
    const navigate = useNavigate();
    const { logOut } = useAccount();
    const sendNotification = useNotification();

    useEffect(() => {
        async function logout() {
            const err = await logOut();
            if (err) {
                sendNotification({
                    description: "Failed to log out :(",
                    type: "error"
                })
            } else {
                sendNotification({
                    description: "Log out successful!",
                    type: "info"
                })
                navigate("/");
            }
        }
        logout();
    }, []);

    return (
        <Backdrop
            sx={{ color: '#fff', zIndex: (theme) => theme.zIndex.drawer + 1 }}
            open={true}
        >
            <CircularProgress color="inherit" />
        </Backdrop> 
    )
}