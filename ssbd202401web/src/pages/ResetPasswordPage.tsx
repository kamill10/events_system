import { Box, Button, Typography } from "@mui/material";
import { Link, useSearchParams } from "react-router-dom";
import { SubmitErrorHandler, SubmitHandler, useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import { ResetPasswordSchema } from "../validation/schemas";
import { useAccount } from "../hooks/useAccount";
import ContainerWithPictureComponent from "../components/ContainerWithPictureComponent";
import CenteredContainerComponent from "../components/CenterdContainerComponent";
import { Pathnames } from "../router/Pathnames";
import FormComponent from "../components/FormComponent";
import TextFieldComponent from "../components/TextFieldComponent";
import { ResetPasswordType } from "../types/Authentication";

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
        <FormComponent
          handleSubmit={handleSubmit}
          onError={onError}
          onSubmit={onSubmit}
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
              sx={{
                mt: 3,
                mb: 2,
              }}
            >
              Reset password
            </Button>
            <Link to={Pathnames.public.home}>Go back to home page</Link>
          </Box>
        </FormComponent>
      </ContainerWithPictureComponent>
    </CenteredContainerComponent>
  );
}
