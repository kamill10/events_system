import {
  Box,
  Button,
  CssBaseline,
  Grid,
  Paper,
  TextField,
  Typography,
} from "@mui/material";
import {
  Controller,
  SubmitErrorHandler,
  SubmitHandler,
  useForm,
} from "react-hook-form";
import { AccountLoginType } from "../types/Account";
import { useAccount } from "../hooks/useAccount";
import { Link } from "react-router-dom";
import { Pathnames } from "../router/Pathnames";
import { yupResolver } from "@hookform/resolvers/yup";
import { LogInSchema } from "../validation/schemas";

export default function LoginPage() {
  const { logIn } = useAccount();
  const {
    handleSubmit,
    control,
    formState: { errors },
    trigger,
  } = useForm<AccountLoginType>({
    defaultValues: {
      username: "",
      password: "",
    },
    resolver: yupResolver(LogInSchema),
  });

  const onSubmit: SubmitHandler<AccountLoginType> = (data) => {
    logIn(data);
  };

  const onError: SubmitErrorHandler<AccountLoginType> = (errors) => {
    console.error(errors);
    alert(errors);
  };

  return (
    <>
      <Grid container component="main" sx={{ height: "85vh" }}>
        <CssBaseline />
        <Grid
          item
          xs={false}
          sm={4}
          md={7}
          sx={{
            backgroundImage:
              "url(https://source.unsplash.com/random?wallpapers)",
            backgroundRepeat: "no-repeat",
            backgroundSize: "cover",
            backgroundPosition: "center",
          }}
        />
        <Grid item xs={12} sm={8} md={5} component={Paper} elevation={6} square>
          <Box
            component={"form"}
            onSubmit={handleSubmit(onSubmit, onError)}
            sx={{
              my: 8,
              mx: 4,
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
            }}
          >
            <Typography component="h1" variant="h3">
              Log in
            </Typography>
            <Box sx={{ mt: 1 }}>
              <Controller
                name="username"
                control={control}
                render={({ field }) => {
                  return (
                    <TextField
                      margin="normal"
                      required
                      fullWidth
                      value={field.value}
                      onChange={(e) => {
                        field.onChange(e);
                        setTimeout(
                          () =>
                            trigger(e.target.name as keyof AccountLoginType),
                          500,
                        );
                      }}
                      id={field.name}
                      label="Username"
                      name={field.name}
                      autoComplete="Username"
                      autoFocus
                      error={errors.username ? true : false}
                    />
                  );
                }}
              />
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
                  return (
                    <TextField
                      margin="normal"
                      required
                      fullWidth
                      value={field.value}
                      onChange={(e) => {
                        field.onChange(e);
                        setTimeout(
                          () =>
                            trigger(e.target.name as keyof AccountLoginType),
                          500,
                        );
                      }}
                      type="password"
                      id={field.name}
                      label="Password"
                      name={field.name}
                      error={errors.password ? true : false}
                      autoComplete="current-password"
                    />
                  );
                }}
              />
              <Typography
                color={"red"}
                fontSize={14}
                width={"inherit"}
                margin={"none"}
              >
                {errors.password?.message}
              </Typography>
              <Button
                type="submit"
                fullWidth
                variant="contained"
                sx={{
                  mt: 3,
                  mb: 2,
                }}
              >
                Log in
              </Button>
              <Grid container>
                <Grid item xs>
                  <Link to={Pathnames.public.forgotPassword}>
                    I forgot my password :(
                  </Link>
                </Grid>
                <Grid item>
                  <Link to={Pathnames.public.signIn}>
                    You don't have an account yet? Sign in!
                  </Link>
                </Grid>
              </Grid>
            </Box>
          </Box>
        </Grid>
      </Grid>
    </>
  );
}
