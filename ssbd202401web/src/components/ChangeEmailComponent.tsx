import { Button, Typography } from "@mui/material";
import { SubmitErrorHandler, SubmitHandler, useForm } from "react-hook-form";
import { useAccount } from "../hooks/useAccount.ts";
import { yupResolver } from "@hookform/resolvers/yup";
import { ChangeMyEmailSchema } from "../validation/schemas.ts";
import { useEffect, useState } from "react";
import VpnKeyIcon from "@mui/icons-material/VpnKey";
import FormComponent from "./FormComponent.tsx";
import TextFieldComponent from "./TextFieldComponent.tsx";
import { ChangeMyEmailType, ChangeMyPasswordType } from "../types/Account.ts";
import ConfirmChangeModal from "./ConfirmChangeModal.tsx";
import { useTranslation } from "react-i18next";

export default function ChangeEmailComponent() {
  const { t } = useTranslation();
  const { updateMyEmail, getMyAccount } = useAccount();
  const [open, setOpen] = useState(false);
  const {
    handleSubmit,
    control,
    formState: { errors },
    trigger,
    getValues,
  } = useForm<ChangeMyEmailType>({
    defaultValues: {
      password: "",
      newEmail: "",
    },
    resolver: yupResolver(ChangeMyEmailSchema),
  });

  useEffect(() => {
    getMyAccount();
  }, []);

  const onSubmit: SubmitHandler<ChangeMyEmailType> = async () => {
    setOpen(true);
  };

  const onError: SubmitErrorHandler<ChangeMyPasswordType> = (_) => {};

  return (
    <>
      <FormComponent
        handleSubmit={handleSubmit}
        onError={onError}
        onSubmit={onSubmit}
        align="start"
      >
        <Typography variant="h4">{t("changeEmail")}</Typography>
        <TextFieldComponent
          control={control}
          errors={errors}
          label={t("currentPassword") + "*"}
          name="password"
          trigger={trigger}
          type="password"
        />
        <TextFieldComponent
          control={control}
          errors={errors}
          label={t("newEmail") + "*"}
          name="newEmail"
          trigger={trigger}
          type="text"
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
        callback={() => updateMyEmail(getValues())}
        handleClose={() => setOpen(false)}
        open={open}
      ></ConfirmChangeModal>
    </>
  );
}
