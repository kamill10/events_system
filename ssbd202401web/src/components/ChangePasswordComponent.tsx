import { Button, Divider, Typography } from "@mui/material";
import { SubmitErrorHandler, SubmitHandler, useForm } from "react-hook-form";
import VpnKeyIcon from "@mui/icons-material/VpnKey";
import { useAccount } from "../hooks/useAccount";
import { useEffect, useState } from "react";
import { yupResolver } from "@hookform/resolvers/yup";
import { ChangeMyPasswordSchema } from "../validation/schemas";
import FormComponent from "./FormComponent.tsx";
import TextFieldComponent from "./TextFieldComponent.tsx";
import { ChangeMyPasswordType } from "../types/Account.ts";
import ConfirmChangeModal from "./ConfirmChangeModal.tsx";

export default function ChangePasswordComponent() {
  const { updateMyPassword, getMyAccount } = useAccount();
  const [open, setOpen] = useState(false);
  const {
    handleSubmit,
    control,
    formState: { errors },
    trigger,
    getValues
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

  const onSubmit: SubmitHandler<ChangeMyPasswordType> = async (_) => {
    setOpen(true);
  };

  const onError: SubmitErrorHandler<ChangeMyPasswordType> = (error) => {
    console.error(error);
  };

  return (
    <>
      <FormComponent
      handleSubmit={handleSubmit}
      onError={onError}
      onSubmit={onSubmit}
      align="start"
    >
      <Typography variant="h4">Change password</Typography>
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
        name="oldPassword"
        trigger={trigger}
        type="password"
      />
      <TextFieldComponent
        control={control}
        errors={errors}
        label="New password"
        name="newPassword"
        trigger={trigger}
        type="password"
      />
      <TextFieldComponent
        control={control}
        errors={errors}
        label="Confirm new password"
        name="confirmNewPassword"
        trigger={trigger}
        type="password"
      />
      <Button
        type="submit"
        variant="contained"
        startIcon={<VpnKeyIcon />}
        sx={{
          mt: 9,
        }}
      >
        Save changes
      </Button>
    </FormComponent>
    <ConfirmChangeModal
      callback={() => updateMyPassword(getValues())}
      handleClose={() => setOpen(false)}
      open={open}
    ></ConfirmChangeModal>
    </>
  );
}
