import { useTranslation } from "react-i18next";
import { useLocations } from "../hooks/useLocations.ts";
import { useEffect, useState } from "react";
import { yupResolver } from "@hookform/resolvers/yup";
import { SubmitErrorHandler, SubmitHandler, useForm } from "react-hook-form";
import { Location, UpdateLocationDataType } from "../types/Location.ts";
import { ChangeLocationDataSchema as changeLocationDataSchema } from "../validation/schemas.ts";
import FormComponent from "./FormComponent.tsx";
import TextFieldComponent from "./TextFieldComponent.tsx";
import { Button, Divider, Typography } from "@mui/material";
import ConfirmChangeModal from "./ConfirmChangeModal.tsx";

export default function ChangeLocationDataComponent({
  location,
  fetchLocation,
}: {
  location: Location | null;
  fetchLocation: () => void;
}) {
  const { t } = useTranslation();
  const { updateLocationById } = useLocations();
  const [open, setOpen] = useState(false);
  const {
    handleSubmit,
    control,
    formState: { errors },
    trigger,
    setValue,
    getValues,
  } = useForm<UpdateLocationDataType>({
    defaultValues: {
      name: location?.name ?? "",
      city: location?.city ?? "",
      country: location?.country ?? "",
      street: location?.street ?? "",
      buildingNumber: location?.buildingNumber ?? "",
      postalCode: location?.postalCode ?? "",
    },
    resolver: yupResolver(changeLocationDataSchema),
  });
  const handleRequest = async () => {
    const err = await updateLocationById(location?.id ?? "", getValues());
    if (!err) {
      fetchLocation();
    }
  };
  const onSubmit: SubmitHandler<UpdateLocationDataType> = async () => {
    setOpen(true);
  };
  const onError: SubmitErrorHandler<UpdateLocationDataType> = (_) => {};
  useEffect(() => {
    setValue("name", location?.name ?? "");
    setValue("city", location?.city ?? "");
    setValue("country", location?.country ?? "");
    setValue("street", location?.street ?? "");
    setValue("buildingNumber", location?.buildingNumber ?? "");
    setValue("postalCode", location?.postalCode ?? "");
  }, [location, setValue, trigger]);
  return (
    <>
      <FormComponent
        handleSubmit={handleSubmit}
        onSubmit={onSubmit}
        onError={onError}
        align="start"
      >
        <Typography variant="h4">{t("changeLocationDetails")}</Typography>
        <Typography variant="body1">{t("enterNewLocationData")}</Typography>
        <Divider
          sx={{
            marginTop: "1rem",
          }}
        ></Divider>
        <TextFieldComponent
          control={control}
          errors={errors}
          name="name"
          label={t("name") + "*"}
          trigger={trigger}
          type="text"
        />
        <TextFieldComponent
          control={control}
          errors={errors}
          name="city"
          label={t("city") + "*"}
          trigger={trigger}
          type="text"
        />
        <TextFieldComponent
          control={control}
          errors={errors}
          label={t("country") + "*"}
          name="country"
          trigger={trigger}
          type="text"
        />
        <TextFieldComponent
          control={control}
          errors={errors}
          label={t("street") + "*"}
          name="street"
          trigger={trigger}
          type="text"
        />
        <TextFieldComponent
          control={control}
          errors={errors}
          label={t("buildingNumber") + "*"}
          name="buildingNumber"
          trigger={trigger}
          type="text"
        />
        <TextFieldComponent
          control={control}
          errors={errors}
          label={t("postalCode") + "*"}
          name="postalCode"
          trigger={trigger}
          type="text"
        />
        <Button
          type="submit"
          variant="contained"
          sx={{
            marginY: 2,
          }}
        >
          {t("saveChanges")}
        </Button>
      </FormComponent>
      <ConfirmChangeModal
        callback={handleRequest}
        handleClose={() => setOpen(false)}
        open={open}
      ></ConfirmChangeModal>
    </>
  );
}
