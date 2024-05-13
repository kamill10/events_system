import { Box, Button, Divider, TextField, Typography } from "@mui/material";
import {
  Controller,
  SubmitErrorHandler,
  SubmitHandler,
  useForm,
} from "react-hook-form";
import VpnKeyIcon from "@mui/icons-material/VpnKey";
import { useAccount } from "../hooks/useAccount";
import { useEffect } from "react";
import { yupResolver } from "@hookform/resolvers/yup";
import { ChangeMyPasswordSchema } from "../validation/schemas";
import { ChangeMyPasswordType } from "../types/ChangeMyPasswordType.ts";

export default function ChangePasswordComponent() {
  const { updateMyPassword, getMyAccount } = useAccount();
  const {
    handleSubmit,
    control,
    formState: { errors },
    trigger,
  } = useForm<ChangeMyPasswordType>({
    defaultValues: {
      oldPassword: "",
      newPassword: "",
      confirmNewPassword: "",
    },
    resolver: yupResolver(ChangeMyPasswordSchema),
  });

  useEffect(() => {
    getMyAccount();
  }, []);

  const onSubmit: SubmitHandler<ChangeMyPasswordType> = async (data) => {
    updateMyPassword(data);
  };

  const onError: SubmitErrorHandler<ChangeMyPasswordType> = (error) => {
    console.error(error);
  };

  return (
    <Box
      component={"form"}
      onSubmit={handleSubmit(onSubmit, onError)}
      margin={"3rem"}
      display={"flex"}
      flexDirection={"column"}
      alignItems={"left"}
    >
      <Typography variant="h4">Change password</Typography>
      <Divider
        sx={{
          marginTop: "3rem",
          marginBottom: "3rem",
        }}
      ></Divider>
      <Controller
        name="oldPassword"
        control={control}
        render={({ field }) => {
          return (
            <TextField
              value={field.value}
              onChange={(e) => {
                field.onChange(e);
                setTimeout(
                  () => trigger(e.target.name as keyof ChangeMyPasswordType),
                  500,
                );
              }}
              id={field.name}
              label="Old password"
              name={field.name}
              error={!!errors.oldPassword}
              type={"password"}
            ></TextField>
          );
        }}
      ></Controller>
      <Typography
        color={"red"}
        fontSize={14}
        width={"inherit"}
        sx={{
          marginBottom: "1rem",
        }}
      >
        {errors.oldPassword?.message}
      </Typography>
      <Controller
        name="newPassword"
        control={control}
        render={({ field }) => {
          return (
            <TextField
              value={field.value}
              onChange={(e) => {
                field.onChange(e);
                setTimeout(
                  () => trigger(e.target.name as keyof ChangeMyPasswordType),
                  500,
                );
              }}
              id={field.name}
              label="New password"
              name={field.name}
              error={!!errors.newPassword}
              type={"password"}
            ></TextField>
          );
        }}
      ></Controller>
      <Typography
        color={"red"}
        fontSize={14}
        width={"inherit"}
        sx={{
          marginBottom: "1rem",
        }}
      >
        {errors.newPassword?.message}
      </Typography>
      <Controller
        name="confirmNewPassword"
        control={control}
        render={({ field }) => {
          return (
            <TextField
              value={field.value}
              onChange={(e) => {
                field.onChange(e);
                setTimeout(
                  () => trigger(e.target.name as keyof ChangeMyPasswordType),
                  500,
                );
              }}
              id={field.name}
              label="Confirm new password"
              name={field.name}
              error={!!errors.confirmNewPassword}
              type={"password"}
            ></TextField>
          );
        }}
      ></Controller>
      <Typography
        color={"red"}
        fontSize={14}
        width={"inherit"}
        sx={{
          marginBottom: "1rem",
        }}
      >
        {errors.confirmNewPassword?.message}
      </Typography>
      <Box
        sx={{
          display: "flex",
          justifyContent: "flex-start",
          gap: "1rem",
          marginTop: "3rem",
        }}
      >
        <Button
          type="submit"
          variant="contained"
          startIcon={<VpnKeyIcon />}
          sx={{
            mt: 1,
            mb: 2,
            width: "fit-content",
            alignSelf: "center",
          }}
        >
          Save changes
        </Button>
      </Box>
    </Box>
  );
}
