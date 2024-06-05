import { Typography } from "@mui/material";
import { useAccount } from "../hooks/useAccount";
import { useEffect } from "react";
import { useTranslation } from "react-i18next";
import ContainerComponent from "../components/ContainerComponent";
import { useEvents } from "../hooks/useEvents";
import Carousel from "react-multi-carousel";
import "react-multi-carousel/lib/styles.css";
import EventCarouselComponent from "../components/EventCarouselComponent";
import { Event } from "../types/Event";
const responsive = {
  superLargeDesktop: {
    // the naming can be any, depends on you.
    breakpoint: { max: 4000, min: 3000 },
    items: 1
  },
  desktop: {
    breakpoint: { max: 3000, min: 1024 },
    items: 1
  },
  tablet: {
    breakpoint: { max: 1024, min: 464 },
    items: 1
  },
  mobile: {
    breakpoint: { max: 464, min: 0 },
    items: 1
  }
};

export default function HomePage() {
  const { getMyAccount, isAuthenticated } = useAccount();
  const { t } = useTranslation();
  const { events, isFetching, getEvents } = useEvents();



  useEffect(() => {
    if (isAuthenticated) getMyAccount();
    getEvents();
  }, []);

  return (
    <ContainerComponent>
      {events != null && <Carousel responsive={responsive} 
      keyBoardControl={true}>
        {events?.map((event: Event) => {
          return <EventCarouselComponent event={event}></EventCarouselComponent>
        })}
      </Carousel>}
    </ContainerComponent>
  );
}
