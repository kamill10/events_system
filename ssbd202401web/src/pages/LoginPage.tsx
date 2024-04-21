import { Box, Button, CircularProgress, Container, CssBaseline, Grid, Link, Paper, TextField, Typography, alpha } from "@mui/material";
import { Controller, SubmitErrorHandler, SubmitHandler, useForm } from "react-hook-form";
import { LoginPagePropType } from "../types/LoginPagePropType";
import { api } from "../axios/axios.config";
import { AccountLogin } from "../types/Account";
import { useMutation } from "@tanstack/react-query";
import { useAtom } from "jotai";
import { atomToken } from "../atoms/tokens";

export default function LoginPage(props: LoginPagePropType) {
    const [_, setToken] = useAtom(atomToken);
    const { isPending, mutate } = useMutation({
      mutationFn: async (formData: AccountLogin) => {
        await api.logIn(formData)
          .then(data => {
            setToken(data.data);
            return data;
          })
          .catch(error => error);
      }
    });
    const { handleSubmit, control } = useForm<AccountLogin>({
      defaultValues: {
        username: "",
        password: ""
      }
    });

    const onSubmit: SubmitHandler<AccountLogin> = (data) => {
      mutate(data);
    };

    const onError: SubmitErrorHandler<AccountLogin> = (errors) => {
      console.error(errors);
    };

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
            backgroundColor: (t) =>
              t.palette.mode === 'light' ? t.palette.grey[50] : t.palette.grey[900],
            backgroundSize: 'cover',
            backgroundPosition: 'center',
          }}
        />
        <Grid item xs={12} sm={8} md={5} component={Paper} elevation={6} square>
          <Box
            component={"form"}
            onSubmit={handleSubmit(onSubmit, onError)}
            sx={{
              my: 8,
              mx: 4,
              display: 'flex',
              flexDirection: 'column',
              alignItems: 'center',
            }}
          >
            <Typography component="h1" variant="h3">
              Log in
            </Typography>
            <Box sx={{ mt: 1 }}>
              <Controller
                name="username"
                control={control}
                render={({field}) => {
                  return <TextField
                    margin="normal"
                    required
                    fullWidth
                    value={field.value}
                    onChange={field.onChange}
                    id={field.name}
                    label="Username"
                    name={field.name}
                    autoComplete="Username"
                    autoFocus
                />
                }}
              />
              <Controller
                name="password"
                control={control}
                render={({field}) => {
                  return <TextField
                    margin="normal"
                    required
                    fullWidth
                    value={field.value}
                    onChange={field.onChange}
                    type="password"
                    id={field.name}
                    label="Password"
                    name={field.name}
                    autoComplete="current-password"
                />
                }}
              />
              <Button
                type="submit"
                fullWidth
                variant="contained"
                sx={{
                  mt: 3,
                  mb: 2,
                  backgroundColor: props.color,
                  ':hover': {
                    backgroundColor: props.color
                  } }}
              >
                Log in
              </Button>
              <Grid container>
                <Grid item xs>
                  <Link href="#" variant="body2" sx={{color: props.color}}>
                    I forgot my password :(
                  </Link>
                </Grid>
                <Grid item>
                  <Link href="/signin" variant="body2" sx={{color: props.color}}>
                    {"You don't have an account yet? Sign in!"}
                  </Link>
                </Grid>
              </Grid>
            </Box>
          </Box>
        </Grid>
        {
          isPending && 
            <Container sx={{
              position: "absolute",
              display: "flex",
              justifyContent: "center",
              alignItems: "center",
              height: "85vh",
              backgroundColor: alpha("#ffffff", 0.85)
           }}>
              <CircularProgress></CircularProgress>  
            </Container>
        }
      </Grid>
    );
}