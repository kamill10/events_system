import { yupResolver } from "@hookform/resolvers/yup";
import { Button, Typography } from "@mui/material";
import { SubmitErrorHandler, SubmitHandler, useForm } from "react-hook-form";
import { ForgotPasswordSchema } from "../validation/schemas";
import { useAccount } from "../hooks/useAccount";
import { Pathnames } from "../router/Pathnames";
import { Link } from "react-router-dom";
import ContainerWithPictureComponent from "../components/ContainerWithPictureComponent";
import FormComponent from "../components/FormComponent";
import TextFieldComponent from "../components/TextFieldComponent";
import { ForgotPasswordType } from "../types/Authentication";
import { useTranslation } from "react-i18next";

export default function ForgotPasswordPage() {
  const { t } = useTranslation();
  const { requestPasswordReset } = useAccount();
  const {
    handleSubmit,
    control,
    formState: { errors },
    trigger,
  } = useForm<ForgotPasswordType>({
    defaultValues: {
      email: "",
    },
    resolver: yupResolver(ForgotPasswordSchema),
  });

  const onSubmit: SubmitHandler<ForgotPasswordType> = (data) => {
    requestPasswordReset(data);
  };

  const onError: SubmitErrorHandler<ForgotPasswordType> = (_) => {};

  return (
    <ContainerWithPictureComponent>
      <FormComponent
        handleSubmit={handleSubmit}
        onSubmit={onSubmit}
        onError={onError}
      >
        <Typography variant="h3" textAlign={"center"}>
          {t("changePassword")}
        </Typography>
        <TextFieldComponent
          control={control}
          errors={errors}
          label="E-mail"
          name="email"
          trigger={trigger}
          type="text"
        />
        <Button
          type="submit"
          variant="contained"
          sx={{
            mt: 3,
            mb: 2,
          }}
        >
          {t("changePassword")}
        </Button>
        <Link to={Pathnames.unauth.login}>{t("goBackToLoginPage")}</Link>
      </FormComponent>
    </ContainerWithPictureComponent>
  );
}
