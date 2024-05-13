import {
  Box,
  Button,
  Container,
  CssBaseline,
  Grid,
  Paper,
  TextField,
  Typography,
} from "@mui/material";
import { useSearchParams } from "react-router-dom";
import {
  Controller,
  SubmitErrorHandler,
  SubmitHandler,
  useForm,
} from "react-hook-form";
import { ResetPasswordType } from "../types/ResetPasswordType";
import { yupResolver } from "@hookform/resolvers/yup";
import { ResetPasswordSchema } from "../validation/schemas";
import { useAccount } from "../hooks/useAccount";

export default function ResetPasswordPage() {
  const [searchParams] = useSearchParams();
  const {
    handleSubmit,
    control,
    trigger,
    formState: { errors },
  } = useForm<ResetPasswordType>({
    defaultValues: {
      token: searchParams.get("token"),
      newPassword: "",
      confirmNewPassword: "",
    },
    resolver: yupResolver(ResetPasswordSchema),
  });
  const { resetMyPassword } = useAccount();

  const onSubmit: SubmitHandler<ResetPasswordType> = (data) => {
    resetMyPassword(data);
  };

  const onError: SubmitErrorHandler<ResetPasswordType> = (error) => {
    console.error(error);
  };

  return (
    <Container
      maxWidth={"xl"}
      sx={{
        display: "flex",
        flexDirection: "column",
        justifyContent: "center",
        alignItemsL: "center",
        height: "100vh",
      }}
    >
      <Box>
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
          <Grid
            item
            xs={12}
            sm={8}
            md={5}
            component={Paper}
            elevation={6}
            square
          >
            <Box
              component={"form"}
              onSubmit={handleSubmit(onSubmit, onError)}
              sx={{
                my: 8,
                mx: 4,
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                justifyContent: "center",
              }}
            >
              <Typography component="h1" variant="h3" textAlign={"center"}>
                Reset your password
              </Typography>
              <Box
                sx={{
                  mt: 1,
                  display: "flex",
                  flexDirection: "column",
                  alignItems: "center",
                }}
              >
                <Controller
                  name="newPassword"
                  control={control}
                  render={({ field }) => {
                    return (
                      <TextField
                        margin="normal"
                        required
                        fullWidth
                        type="password"
                        value={field.value}
                        onChange={(e) => {
                          field.onChange(e);
                          setTimeout(
                            () =>
                              trigger(e.target.name as keyof ResetPasswordType),
                            500,
                          );
                        }}
                        id={field.name}
                        label="New password"
                        name={field.name}
                        error={errors.newPassword ? true : false}
                        autoComplete="new-password"
                        autoFocus
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
                  {errors.newPassword?.message}
                </Typography>
                <Controller
                  name="confirmNewPassword"
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
                              trigger(e.target.name as keyof ResetPasswordType),
                            500,
                          );
                        }}
                        type="password"
                        id={field.name}
                        label="Confirm new password"
                        name={field.name}
                        error={errors.confirmNewPassword ? true : false}
                        autoComplete="confirm-new-password"
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
                  {errors.confirmNewPassword?.message}
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
      </Box>
    </Container>
  );
}
