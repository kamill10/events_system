import { Breadcrumbs, Divider, Tab, Tabs, Typography } from "@mui/material";
import ContainerComponent from "../components/ContainerComponent";
import { Link } from "react-router-dom";
import { SyntheticEvent, useState } from "react";
import { useTranslation } from "react-i18next";
import LocationsComponent from "../components/LocationsComponent.tsx";
import DeletedLocationsComponent from "../components/DeletedLocationsComponent.tsx";
import { useLocations } from "../hooks/useLocations.ts";

export default function LocationsPage() {
  const { t } = useTranslation();
  const {
    locations,
    getLocationsWithPagination,
    addLocation,
    getDeletedLocationsWithPagination,
    getDeletedLocation,
    restoreLocation,
  } = useLocations();
  const [page, setPage] = useState(0);
  const handleChange = (_: SyntheticEvent, newValue: number) => {
    setPage(newValue);
  };

  return (
    <ContainerComponent>
      <Breadcrumbs aria-label="breadcrumb" sx={{ marginBottom: 3 }}>
        <Link to="/home" style={{ textDecoration: "none", color: "black" }}>
          {t("home")}
        </Link>
        <Link
          to="/locations"
          style={{
            textDecoration: "none",
            color: "black",
            fontWeight: "bold",
          }}
        >
          {t("locations")}
        </Link>
        <Typography color="grey">{t("locationDetails")}</Typography>
      </Breadcrumbs>

      <Divider></Divider>
      <Tabs value={page} onChange={handleChange}>
        <Tab label={t("locations")}></Tab>
        <Tab label={t("deletedLocations")}></Tab>
      </Tabs>
      {page == 0 && (
        <LocationsComponent
          locations={locations}
          fetchLocations={getLocationsWithPagination}
          addLocation={addLocation}
        />
      )}
      {page == 1 && (
        <DeletedLocationsComponent
          locations={locations}
          fetchLocations={getDeletedLocationsWithPagination}
          restoreLocation={restoreLocation}
          getDeletedLocation={getDeletedLocation}
        />
      )}
    </ContainerComponent>
  );
}
