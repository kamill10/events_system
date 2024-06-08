import {Breadcrumbs, Button, Divider, Tab, Tabs, Typography} from "@mui/material";
import ContainerComponent from "../components/ContainerComponent";
import { useTranslation } from "react-i18next";
import { SyntheticEvent, useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import useNotification from "../hooks/useNotification.tsx";
import ChangeLocationDetails from "../components/ChangeLocationDetails.tsx";
import {Pathnames} from "../router/Pathnames.ts";
import {useLocations} from "../hooks/useLocations.ts";
import {Location} from "../types/Location.ts";
import RefreshIcon from "@mui/icons-material/Refresh";

export default function LocationPage() {
  const { t } = useTranslation();
  const { id } = useParams<{ id: string }>();
  const [location, setLocation] = useState<Location | null>(null);
  const { getLocationById } = useLocations();
  const sendNotification = useNotification();
  const [page, setPage] = useState(0);
  const handleChange = (_: SyntheticEvent, newValue: number) => {
    setPage(newValue);
  };

    async function fetchLocation() {
        if (id) {
            getLocationById(id)
                .then((location) => {
                    setLocation(location);
                });
        } else {
            sendNotification({
                type: "error",
                description: t("noURLParam"),
            });
        }
    }

    useEffect(() => {
        fetchLocation();
    }, []);

  return (
    <ContainerComponent>
      <Breadcrumbs
        aria-label="breadcrumb"
        sx={{
          marginBottom: 3,
        }}
      >
        <Link
          to={Pathnames.public.home}
          style={{
            textDecoration: "none",
            color: "black",
          }}
        >
          {t("home")}
        </Link>
        <Link
          to={Pathnames.manager.locations}
          style={{
            textDecoration: "none",
            color: "black",
          }}
        >
          {t("locations")}
        </Link>
        <Typography
          color="text.primary"
          sx={{
            fontWeight: "bold",
          }}
        >
          {t("locationDetails")}
        </Typography>
      </Breadcrumbs>
        <Button
            variant="contained"
            onClick={fetchLocation}
            startIcon={<RefreshIcon />}
            sx={{
                margin: 2,
            }}
        >
            {t("refreshData")}
        </Button>
      <Tabs value={page} onChange={handleChange}>
        <Tab label={t("changeLocationDetails")}></Tab>
      </Tabs>

      <Divider></Divider>
      {page == 0 && (
        <ChangeLocationDetails
          location={location}
          fetchLocation={fetchLocation}
        ></ChangeLocationDetails>
      )}
    </ContainerComponent>
  );
}
