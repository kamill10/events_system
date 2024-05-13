import {
  Box,
  Button,
  TextField,
  Typography,
} from "@mui/material";
import { Link, useSearchParams } from "react-router-dom";
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
import ContainerWithPictureComponent from "../components/ContainerWithPictureComponent";
import CenteredContainerComponent from "../components/CenterdContainerComponent";
import { Pathnames } from "../router/Pathnames";

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
    <CenteredContainerComponent>
      <ContainerWithPictureComponent>
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
          <Link to={Pathnames.public.home}>Go back to home page</Link>
        </Box>
      </Box>
      </ContainerWithPictureComponent>
    </CenteredContainerComponent>
  );
}
