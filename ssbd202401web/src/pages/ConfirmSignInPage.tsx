import { Box, Typography } from "@mui/material";
import { useEffect, useState } from "react";
import { Link, useSearchParams } from "react-router-dom";
import { useAccount } from "../hooks/useAccount";

export default function ConfirmSignInPage() {
    const [searchParams] = useSearchParams();
    const { confirmSignIn } = useAccount();
    const [failed, setFailed] = useState(false);

    useEffect(() => {
        console.log(searchParams.get("key"));
    }, [searchParams]);

    useEffect(() => {
        async function foo() {
            const key = searchParams.get("key");
            if (!key) {
                alert("No parameter of name key!"); 
            } else {
                const err = await confirmSignIn(key);
                if (err) {
                    setFailed(true);
                }
            }
        }

        foo();
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
                        There was an error while activating your account. If you think this is a mistake, contact support.
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