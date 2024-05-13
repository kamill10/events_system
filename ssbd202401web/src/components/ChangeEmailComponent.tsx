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
import { ChangeMyEmailSchema } from "../validation/schemas.ts";
import { useEffect } from "react";
import VpnKeyIcon from "@mui/icons-material/VpnKey";
import { ChangeMyEmailType } from "../types/ChangeMyEmailType.ts";
import FormComponent from "./FormComponent.tsx";
import TextFieldComponent from "./TextFieldComponent.tsx";

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
    <FormComponent
      handleSubmit={handleSubmit}
      onError={onError}
      onSubmit={onSubmit}
      align="start"
    >
      <Typography variant="h4">Change email</Typography>
      <Divider
        sx={{
          marginTop: "3rem",
          marginBottom: "3rem",
        }}
      ></Divider>
      <TextFieldComponent
        control={control}
        errors={errors}
        label="Current password"
        name="password"
        trigger={trigger}
        type="password"
      />
      <TextFieldComponent
        control={control}
        errors={errors}
        label="New E-mail"
        name="email"
        trigger={trigger}
        type="text"
      />
      <Button
          type="submit"
          variant="contained"
          startIcon={<VpnKeyIcon />}
          sx={{
            mt: 9
          }}
        >
          Save changes
        </Button>
    </FormComponent>
  );
}
