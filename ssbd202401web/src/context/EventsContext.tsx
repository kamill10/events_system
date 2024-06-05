import { ReactNode, createContext, useContext, useState } from "react";

import { Event } from "../types/Event";

interface ManageEvents {
  events: Event[] | null;
  setEvents: (events: Event[]) => void;
  isFetching: boolean;
  setIsFetching: (state: boolean) => void;
}

const EventsStateContext = createContext<ManageEvents | null>(null);

export const EventsStateContextProvider = ({
  children,
}: {
  children: ReactNode;
}) => {
  const [events, setEvents] = useState<Event[] | null>(null);
  const [isFetching, setIsFetching] = useState(false);

  return (
    <EventsStateContext.Provider
      value={{
        events,
        setEvents,
        isFetching,
        setIsFetching,
      }}
    >
      {children}
    </EventsStateContext.Provider>
  );
};

export const useEventsState = () => {
  const eventsState = useContext(EventsStateContext);

  if (!eventsState) {
    throw new Error("You forgot about EventsStateContext");
  }

  return eventsState;
};
