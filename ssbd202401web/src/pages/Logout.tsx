import { useNavigate } from "react-router-dom"
import { useAccount } from "../hooks/useAccount"
import { useEffect } from "react";
import { Pathnames } from "../router/Pathnames";

export default function Logout() {
    const navigate = useNavigate();
    const { logOut } = useAccount();

    useEffect(() => {
        logOut();
        navigate(Pathnames.public.home);
    }, []);

    return <>
    </>
}