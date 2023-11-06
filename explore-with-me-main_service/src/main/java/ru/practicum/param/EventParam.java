package ru.practicum.param;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import lombok.Data;
import ru.practicum.model.event.EventSort;
import ru.practicum.model.event.EventState;
import ru.practicum.model.event.QEvent;
import ru.practicum.model.user.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Data
public class EventParam {

    private final List<Predicate> predicates = new LinkedList<>();
    private EventSort sort = EventSort.NONE;
    private Collection<Long> initiatorsId;

    public void setSearchingText(String searchingText) {
        if (searchingText != null) {
            predicates.add(QEvent.event.annotation.containsIgnoreCase(searchingText)
                    .or(QEvent.event.description.containsIgnoreCase(searchingText)));
        }
    }

    public void setOnlyAvailable(Boolean onlyAvailable) {
        if (onlyAvailable != null && onlyAvailable) {
            predicates.add(QEvent.event.participantLimit.eq(0)
                    .or(QEvent.event.confirmedRequests.size().loe(QEvent.event.participantLimit)));
        }
    }

    public void setInitiatorsId(Collection<Long> initiatorsId) {
        if (initiatorsId != null) {
            this.initiatorsId = initiatorsId;
            predicates.add(QEvent.event.initiator.id.in(initiatorsId));
        }
    }

    public void setCategoriesId(Collection<Long> categoriesId) {
        if (categoriesId != null) {
            predicates.add(QEvent.event.category.id.in(categoriesId));
        }
    }

    public void setRangeEnd(LocalDateTime rangeEnd) {
        if (rangeEnd != null) {
            predicates.add(QEvent.event.eventDate.before(rangeEnd));
        }
    }

    public void setStates(EventState... states) {
        if (states != null) {
            predicates.add(QEvent.event.state.in(states));
        }
    }

    public void setStates(Collection<EventState> states) {
        if (states != null) {
            predicates.add(QEvent.event.state.in(states));
        }
    }

    public void setRangeStart(LocalDateTime rangeStart) {
        if (rangeStart != null) {
            predicates.add(QEvent.event.eventDate.after(rangeStart));
        }

    }

    public void setPaid(Boolean paid) {
        if (paid != null) {
            predicates.add(QEvent.event.paid.eq(paid));
        }
    }

    public Predicate getExpression() {
        if (predicates.isEmpty()) {
            return Expressions.asBoolean(true).isTrue();
        }
        return ExpressionUtils.allOf(predicates);
    }

    public void setBySubscriber(User user) {
        if (user != null) {
            predicates.add(QEvent.event.initiator.subscribers.contains(user));
        }
    }
}
