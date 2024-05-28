import { Button, Typography } from "@mui/material";
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
import { useTranslation } from "react-i18next";

export default function ChangePasswordComponent() {
  const { t } = useTranslation();
  const { updateMyPassword, getMyAccount } = useAccount();
  const [open, setOpen] = useState(false);
  const {
    handleSubmit,
    control,
    formState: { errors },
    trigger,
    getValues,
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

  const onSubmit: SubmitHandler<ChangeMyPasswordType> = async () => {
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
        <Typography variant="h4">{t("changePassword")}</Typography>
        <TextFieldComponent
          control={control}
          errors={errors}
          label={t("currentPassword") + "*"}
          name="oldPassword"
          trigger={trigger}
          type="password"
        />
        <TextFieldComponent
          control={control}
          errors={errors}
          label={t("newPassword") + "*"}
          name="newPassword"
          trigger={trigger}
          type="password"
        />
        <TextFieldComponent
          control={control}
          errors={errors}
          label={t("confirmNewPassword") + "*"}
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
          {t("saveChanges")}
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
