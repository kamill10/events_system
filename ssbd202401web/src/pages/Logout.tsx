import { useNavigate } from "react-router-dom"
import { useAccount } from "../hooks/useAccount"
import { Backdrop, CircularProgress } from "@mui/material";
import { useEffect } from "react";

export default function Logout() {
    const navigate = useNavigate();
    const { logOut } = useAccount();

    useEffect(() => {
        logOut();
        navigate("/");
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