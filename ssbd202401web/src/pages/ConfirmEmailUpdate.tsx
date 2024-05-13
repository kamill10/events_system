import {Link, useSearchParams} from "react-router-dom";
import {useAccount} from "../hooks/useAccount.ts";
import {useState} from "react";
import {Box, Button, Typography} from "@mui/material";

export function ConfirmEmailUpdate() {
    const [searchParams] = useSearchParams();
    const { confirmEmailUpdate } = useAccount();
    const [failed, setFailed] = useState(false);
    const [pressed, setPressed] = useState(false);

    async function verify() {
        setPressed(true);
        const key = searchParams.get("token");
        if (!key) {
            setFailed(true);
        } else {
            const err = await confirmEmailUpdate(key);
            if (err) {
                setFailed(true);
            }
        }
    }

    if (failed) {
        return (
            <Box
                component={"div"}
                sx={{
                    display: "flex",
                    justifyContent: "center",
                    alignItems: "center",
                    height: "100vh",
                }}
            >
                <Box
                    component={"div"}
                    sx={{
                        boxShadow: 6,
                        padding: "3rem",
                    }}
                >
                    <Typography variant="h3" textAlign={"center"}>
                        Something happened :((
                    </Typography>
                    <Typography variant="h6" textAlign={"center"}>
                        There was an error while confirming your email change. If you
                        think this is a mistake, contact support.
                    </Typography>
                    <Typography variant="h6" textAlign={"center"}>
                        <Link to={"/"}>Go to home page</Link>
                    </Typography>
                </Box>
            </Box>
        );
    }

    if (!pressed) {
        return (
            <Box
                component={"div"}
                sx={{
                    display: "flex",
                    justifyContent: "center",
                    alignItems: "center",
                    height: "100vh",
                }}
            >
                <Box
                    component={"div"}
                    sx={{
                        boxShadow: 6,
                        padding: "3rem",
                        display: "flex",
                        flexDirection: "column",
                        alignItems: "center",
                    }}
                >
                    <Typography variant="h3" textAlign={"center"}>
                        Confirm your email change!
                    </Typography>
                    <Typography variant="h6" textAlign={"center"}>
                        Click the button below to confirm your email change!
                    </Typography>
                    <Button
                        onClick={verify}
                        variant="contained"
                        sx={{
                            mt: 3,
                            mb: 2,
                        }}
                    >
                        Verify
                    </Button>
                    <Typography variant="h6" textAlign={"center"}>
                        <Link to={"/"}>Go to home page</Link>
                    </Typography>
                </Box>
            </Box>
        );
    }

    return (
        <Box
            component={"div"}
            sx={{
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                height: "100vh",
            }}
        >
            <Box
                component={"div"}
                sx={{
                    boxShadow: 6,
                    padding: "3rem",
                }}
            >
                <Typography variant="h3" textAlign={"center"}>
                    Email has been changed!
                </Typography>
                <Typography variant="h6" textAlign={"center"}>
                    Go back to your&#x20;
                    <Link to={"/profile"}>profile</Link>
                </Typography>
            </Box>
        </Box>
    );
}