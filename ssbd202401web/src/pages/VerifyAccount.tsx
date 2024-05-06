import { Box, Typography } from "@mui/material";
import { useEffect, useState } from "react";
import { Link, useSearchParams } from "react-router-dom";
import { useAccount } from "../hooks/useAccount";

export default function VerifyAccount() {
    const [searchParams] = useSearchParams();
    const { verifyAccount } = useAccount();
    const [failed, setFailed] = useState(false);

    useEffect(() => {
        async function verify() {
            const key = searchParams.get("token");
            if (!key) {
                setFailed(true);
            } else {
                const err = await verifyAccount(key);
                if (err) {
                    setFailed(true);
                }
            }
        }

        verify();
    }, []);

    if (failed) {
        return (
            <Box
                component={"div"}
                sx={{
                    display: "flex",
                    justifyContent: "center",
                    alignItems: "center",
                    height: "100vh"
                }}
            >
                <Box
                    component={"div"}
                    sx={{
                        boxShadow: 6,
                        padding: "3rem"
                    }}
                >
                    <Typography variant="h3" textAlign={"center"}>
                        Something happened :((
                    </Typography>
                    <Typography variant="h6" textAlign={"center"}>
                        There was an error while verifying your account. If you think this is a mistake, contact support.
                    </Typography>
                    <Typography variant="h6" textAlign={"center"}>
                        <Link to={"/"}>Go to home page</Link>
                </Typography>
                </Box>
            </Box>
        )
    }

    return (
        <Box
            component={"div"}
            sx={{
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                height: "100vh"
            }}
        >
            <Box
                component={"div"}
                sx={{
                    boxShadow: 6,
                    padding: "3rem"
                }}
            >
                <Typography variant="h3" textAlign={"center"}>
                    Account has been activated! 
                </Typography>
                <Typography variant="h6" textAlign={"center"}>
                    You can go to login page manually, or&#x20;
                    <Link to={"/login"}>click here!</Link>
                </Typography>
            </Box>
        </Box>
    );
}