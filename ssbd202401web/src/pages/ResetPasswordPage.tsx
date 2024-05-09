import { Box, Button, CssBaseline, Grid, Link, Paper, TextField, Typography } from "@mui/material";
import { useState, useEffect } from "react";
import { useSearchParams } from "react-router-dom";
import { useAccount } from "../hooks/useAccount";
import { Controller, SubmitErrorHandler, SubmitHandler, useForm } from "react-hook-form";
import { ForgotPasswordType } from "../types/ForgotPassword";
import { ResetPasswordType } from "../types/ResetPasswordType";
import { yupResolver } from "@hookform/resolvers/yup";
import { ForgotPasswordSchema } from "../validation/schemas";

export default function ResetPasswordPage() {
    const [searchParams] = useSearchParams();
    const { resetMyPassword } = useAccount();
    const { handleSubmit, control, formState: { errors }, trigger } = useForm<ForgotPasswordType>({
        defaultValues: {
            email: ""
        },
        resolver: yupResolver(ForgotPasswordSchema)
    });

    const onSubmit: SubmitHandler<ResetPasswordType> = (data) => {
        resetMyPassword(data);
    }

    const onError: SubmitErrorHandler<ResetPasswordType> = (error) => {
        console.error(error);
    }

    return (
        <Grid container component="main" sx={{ height: '85vh' }}>
            <CssBaseline />
            <Grid
                item
                xs={false}
                sm={4}
                md={7}
                sx={{
                    backgroundImage: 'url(https://source.unsplash.com/random?wallpapers)',
                    backgroundRepeat: 'no-repeat',
                    backgroundSize: 'cover',
                    backgroundPosition: 'center',
                }}
            />
            <Grid item xs={12} sm={8} md={5} component={Paper} elevation={6} square>
                <Box
                    sx={{
                        my: 8,
                        mx: 4,
                        display: 'flex',
                        flexDirection: 'column',
                        alignItems: 'center',
                    }}
                >
                    <Typography variant="h3"
                        marginBottom={15}
                    >
                        Reset your password
                    </Typography>
                    <Box
                        component={"form"}
                        sx={{
                            display: 'flex',
                            flexDirection: 'column',
                            alignItems: 'center',
                        }}
                    >
                        <Button
                            type="submit"
                            variant="contained"
                            sx={{
                                mt: 3,
                                mb: 2,
                            }}
                        >
                            Reset password
                        </Button>
                    </Box>
                </Box>
            </Grid>
        </Grid>

    )
}