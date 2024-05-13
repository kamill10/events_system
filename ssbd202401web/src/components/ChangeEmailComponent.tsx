import { Box, Button, Divider, TextField, Typography } from "@mui/material";
import {
  Controller,
  SubmitErrorHandler,
  SubmitHandler,
  useForm,
} from "react-hook-form";
import { useAccount } from "../hooks/useAccount.ts";
import { ChangeMyPasswordType } from "../types/ChangeMyPasswordType.ts";
import { yupResolver } from "@hookform/resolvers/yup";
import {ChangeMyEmailSchema} from "../validation/schemas.ts";
import { useEffect } from "react";
import VpnKeyIcon from "@mui/icons-material/VpnKey";
import {ChangeMyEmailType} from "../types/ChangeMyEmailType.ts";

export default function ChangeEmailComponent() {
  const { updateMyEmail, getMyAccount } = useAccount();
  const {
    handleSubmit,
    control,
    formState: { errors },
    trigger,
  } = useForm<ChangeMyEmailType>({
    defaultValues: {
      password: "",
        email: "",
    },
    resolver: yupResolver(ChangeMyEmailSchema),
  });

  useEffect(() => {
    getMyAccount();
  }, []);

  const onSubmit: SubmitHandler<ChangeMyEmailType> = async (data) => {
    updateMyEmail(data);
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
      <Typography variant="h4">Change email</Typography>
      <Divider
        sx={{
          marginTop: "3rem",
          marginBottom: "3rem",
        }}
      ></Divider>
      <Controller
        name="password"
        control={control}
        render={({ field }) => {
          return (
            <TextField
              value={field.value}
              onChange={(e) => {
                field.onChange(e);
                setTimeout(
                  () => trigger(e.target.name as keyof ChangeMyEmailType),
                  500,
                );
              }}
              id={field.name}
              label="Password"
              name={field.name}
              error={!!errors.password}
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
        {errors.password?.message}
      </Typography>
      <Controller
        name="email"
        control={control}
        render={({ field }) => {
          return (
            <TextField
              value={field.value}
              onChange={(e) => {
                field.onChange(e);
                setTimeout(
                  () => trigger(e.target.name as keyof ChangeMyEmailType),
                  500,
                );
              }}
              id={field.name}
              label="New email"
              name={field.name}
              error={!!errors.email}
              type={"text"}
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
        {errors.email?.message}
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
