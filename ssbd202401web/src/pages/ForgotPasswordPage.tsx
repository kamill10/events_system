import { yupResolver } from "@hookform/resolvers/yup";
import {
  Box,
  Button,
  CssBaseline,
  Grid,
  Paper,
  TextField,
  Typography,
} from "@mui/material";
import {
  Controller,
  SubmitErrorHandler,
  SubmitHandler,
  useForm,
} from "react-hook-form";
import { ForgotPasswordSchema } from "../validation/schemas";
import { ForgotPasswordType } from "../types/ForgotPassword";
import { useAccount } from "../hooks/useAccount";
import { Pathnames } from "../router/Pathnames";
import { Link } from "react-router-dom";
import ContainerWithPictureComponent from "../components/ContainerWithPictureComponent";
import FormComponent from "../components/FormComponent";

export default function ForgotPasswordPage() {
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

  const onError: SubmitErrorHandler<ForgotPasswordType> = (error) => {
    console.error(error);
  };

  return (
    <ContainerWithPictureComponent>
      <FormComponent
        handleSubmit={handleSubmit}
        onSubmit={onSubmit}
        onError={onError}
      >
        <Typography variant="h3" marginBottom={15} textAlign={"center"}>
            Reset your password
          </Typography>
        <Controller
              name="email"
              control={control}
              render={({ field }) => {
                return (
                  <TextField
                    fullWidth
                    value={field.value}
                    onChange={(e) => {
                      field.onChange(e);
                      setTimeout(
                        () =>
                          trigger(e.target.name as keyof ForgotPasswordType),
                        500,
                      );
                    }}
                    id={field.name}
                    label="E-mail"
                    name={field.name}
                    autoComplete=""
                    error={errors.email ? true : false}
                  ></TextField>
                );
              }}
            ></Controller>
            <Typography
              color={"red"}
              fontSize={14}
              width={"inherit"}
              margin={"none"}
            >
              {errors.email?.message}
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

      </FormComponent>
    </ContainerWithPictureComponent>
  );
}
