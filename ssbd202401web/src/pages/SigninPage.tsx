import { Box, Button, CssBaseline, Grid, MenuItem, Paper, TextField, Typography } from "@mui/material";
import { useAccount } from "../hooks/useAccount";
import { Controller, SubmitErrorHandler, SubmitHandler, useForm } from "react-hook-form";
import { AccountSingInType } from "../types/Account";
import { GenderEnum } from "../types/enums/Gender.enum";
import { signInValidationSchema } from "../validation/schemas";
import { yupResolver } from "@hookform/resolvers/yup";
import { Pathnames } from "../router/Pathnames";
import { Link } from "react-router-dom";

export default function SigninPage() {
    const { signIn } = useAccount();
    const { handleSubmit, control, formState: { errors }, trigger } = useForm<AccountSingInType>({
        defaultValues: {
            username: "",
            password: "",
            email: "",
            gender: 0,
            firstName: "",
            lastName: "",
            language: navigator.language == "pl" ? "POLISH" : "ENGLISH"
        },
        resolver: yupResolver(signInValidationSchema)
    });

    const onSubmit: SubmitHandler<AccountSingInType> = async (data) => {
        await signIn(data);
    }

    const onError: SubmitErrorHandler<AccountSingInType> = (errors) => {
        console.error(errors);
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
                    component={"form"}
                    onSubmit={handleSubmit(onSubmit, onError)}
                    sx={{
                        display: "flex",
                        flexDirection: "column",
                        alignItems: "center",
                        paddingTop: "3rem",
                        marginX: "5rem"
                    }}
                >
                    <Typography variant="h3" component={"h1"}>
                        Sign in
                    </Typography>
                    <Controller
                        name="firstName"
                        control={control}
                        render={({ field }) => {
                            return <TextField
                                sx={{ marginTop: "1rem" }}
                                value={field.value}
                                onChange={(e) => {
                                    field.onChange(e);
                                    setTimeout(() => trigger(e.target.name as keyof AccountSingInType), 500);
                                }}
                                id={field.name}
                                label="First name"
                                name={field.name}
                                autoComplete=""
                                fullWidth
                                error={errors.firstName ? true : false}
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
                        {errors.firstName?.message}
                    </Typography>
                    <Controller
                        name="lastName"
                        control={control}
                        render={({ field }) => {
                            return <TextField
                                sx={{ marginTop: "1rem" }}
                                value={field.value}
                                onChange={(e) => {
                                    field.onChange(e);
                                    setTimeout(() => trigger(e.target.name as keyof AccountSingInType), 500);
                                }}
                                id={field.name}
                                label="Last name"
                                name={field.name}
                                autoComplete=""
                                fullWidth
                                error={errors.lastName ? true : false}
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
                        {errors.lastName?.message}
                    </Typography>
                    <Controller
                        name="email"
                        control={control}
                        render={({ field }) => {
                            return <TextField
                                sx={{ marginTop: "1rem" }}
                                value={field.value}
                                onChange={(e) => {
                                    field.onChange(e);
                                    setTimeout(() => trigger(e.target.name as keyof AccountSingInType), 500);
                                }}
                                id={field.name}
                                label="E-mail"
                                name={field.name}
                                autoComplete=""
                                fullWidth
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
                    <Controller
                        name="username"
                        control={control}
                        render={({ field }) => {
                            return <TextField
                                sx={{ marginTop: "1rem" }}
                                value={field.value}
                                onChange={(e) => {
                                    field.onChange(e);
                                    setTimeout(() => trigger(e.target.name as keyof AccountSingInType), 500);
                                }}
                                id={field.name}
                                label="Username"
                                name={field.name}
                                autoComplete=""
                                fullWidth
                                error={errors.username ? true : false}
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
                        {errors.username?.message}
                    </Typography>
                    <Controller
                        name="password"
                        control={control}
                        render={({ field }) => {
                            return <TextField
                                sx={{ marginTop: "1rem" }}
                                type="password"
                                value={field.value}
                                onChange={(e) => {
                                    field.onChange(e);
                                    setTimeout(() => trigger(e.target.name as keyof AccountSingInType), 500);
                                }}
                                id={field.name}
                                label="Password"
                                name={field.name}
                                autoComplete=""
                                fullWidth
                                error={errors.password ? true : false}
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
                        {errors.password?.message}
                    </Typography>
                    <Controller
                        name="gender"
                        control={control}
                        render={({ field }) => {
                            return <TextField
                                select
                                label="Gender"
                                value={field.value}
                                onChange={(e) => {
                                    field.onChange(e);
                                    setTimeout(() => trigger(e.target.name as keyof AccountSingInType), 500);
                                }}
                                id={field.name}
                                sx={{ marginTop: "1rem" }}
                                name={field.name}
                                autoComplete=""
                            >
                                {Object.keys(GenderEnum).map((key, value) => {
                                    return <MenuItem key={key} value={value}>
                                        {GenderEnum[key as keyof typeof GenderEnum].info}
                                    </MenuItem>
                                })}
                            </TextField>
                        }}
                    >
                    </Controller>
                    <Button
                        type="submit"
                        variant="contained"
                        sx={{
                            mt: 3,
                            mb: 2,
                        }}
                    >
                        Sign in
                    </Button>
                    <Link to={Pathnames.public.login}>
                        Already have an account? Log in!
                    </Link>
                </Box>
            </Grid>
        </Grid>

    );
}