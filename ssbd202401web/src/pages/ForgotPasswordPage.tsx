import { yupResolver } from "@hookform/resolvers/yup";
import { Box, Button, CssBaseline, Grid, Paper, TextField, Typography } from "@mui/material";
import { Controller, SubmitErrorHandler, SubmitHandler, useForm } from "react-hook-form";
import { ForgotPasswordSchema } from "../validation/schemas";
import { ForgotPasswordType } from "../types/ForgotPassword";
import { useAccount } from "../hooks/useAccount";

export default function ForgotPasswordPage() {
    const { requestPasswordReset } = useAccount();
    const { handleSubmit, control, formState: { errors }, trigger } = useForm<ForgotPasswordType>({
        defaultValues: {
            email: ""
        },
        resolver: yupResolver(ForgotPasswordSchema)
    });

    const onSubmit: SubmitHandler<ForgotPasswordType> = (data) => {
        requestPasswordReset(data);
    }

    const onError: SubmitErrorHandler<ForgotPasswordType> = (error) => {
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
                        onSubmit={handleSubmit(onSubmit, onError)}
                        sx={{
                            display: 'flex',
                            flexDirection: 'column',
                            alignItems: 'center',
                        }}
                    >
                    <Controller
                        name="email"
                        control={control}
                        render={({field}) => {
                            return <TextField
                                value={field.value}
                                onChange={(e) => {
                                    field.onChange(e);
                                    setTimeout(() => trigger(e.target.name as keyof ForgotPasswordType), 500);
                                }}
                                id={field.name}
                                label="E-mail"
                                name={field.name}
                                autoComplete=""
                                error={errors.email ? true : false}
                            >

                            </TextField>
                        }}
                    >
                    </Controller>
                    <Typography
                        color={"red"}
                        fontSize={14}
                        width={"inherit"}
                        margin={"none"}
                    >
                        {errors.email?.message}
                    </Typography>
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