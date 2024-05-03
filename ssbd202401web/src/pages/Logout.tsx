import { useNavigate } from "react-router-dom"
import { useAccount } from "../hooks/useAccount"
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

    return <>
    </>
}