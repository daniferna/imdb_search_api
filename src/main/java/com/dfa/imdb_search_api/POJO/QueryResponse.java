package com.dfa.imdb_search_api.POJO;

import com.dfa.imdb_search_api.POJO.aggregations.Aggregation;
import com.dfa.imdb_search_api.POJO.aggregations.bucket.impl.DateHistogramBucket;
import com.dfa.imdb_search_api.POJO.aggregations.bucket.impl.TermBucket;
import com.dfa.imdb_search_api.POJO.serializers.QueryResponseSerializer;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Arrays;
import java.util.StringJoiner;

import static java.lang.System.arraycopy;

@JsonSerialize(using = QueryResponseSerializer.class)
public class QueryResponse {

    private final long total;
    private final Film[] items;
    private final Aggregation<TermBucket>[] termAggregations;
    private final Aggregation<DateHistogramBucket> dateHistogramAggregation;
    private final JsonNode suggestion;

    @JsonCreator()
    public QueryResponse(@JsonProperty long total, @JsonProperty Film[] items,
                         @JsonProperty Aggregation<TermBucket>[] termAggregations,
                         @JsonProperty Aggregation<DateHistogramBucket> dateHistogram,
                         @JsonProperty JsonNode suggestion) {
        this.total = total;
        this.items = items;
        this.termAggregations = termAggregations;
        this.dateHistogramAggregation = dateHistogram;
        this.suggestion = suggestion;
    }

    public JsonNode getSuggestion() {
        return suggestion;
    }

    public long getTotal() {
        return total;
    }

    public Film[] getItems() {
        return items;
    }

    public Aggregation<TermBucket>[] getTermAggregations() {
        return termAggregations;
    }

    /**
     * Method that return all aggregations in a unique array.
     *
     * @return An array containing all the aggregations of this response.
     */
    public Aggregation<?>[] getAggregations() {
        Aggregation<?>[] allAggregations = new Aggregation[termAggregations.length + 1];
        arraycopy(termAggregations, 0, allAggregations, 0, termAggregations.length);
        allAggregations[termAggregations.length] = dateHistogramAggregation;
        return allAggregations;
    }

    /**
     * Method that returns the term aggregation passed by params, if it doesn't exist returns an empty aggregation
     *
     * @param nameOfAggregation The name of the aggregation
     * @return The aggregation if found, empty aggregation if not
     */
    public Aggregation<TermBucket> findTermAggregation(String nameOfAggregation) {
        return Arrays.stream(termAggregations).filter(ag -> ag.getName().equals(nameOfAggregation))
                .findFirst().orElse(new Aggregation<>(nameOfAggregation, new TermBucket[]{}));
    }

    public Aggregation<DateHistogramBucket> getDateHistogramAggregation() {
        return dateHistogramAggregation;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", QueryResponse.class.getSimpleName() + "[", "]")
                .add("total=" + total)
                .add("items=" + Arrays.toString(items))
                .add("termAggregations=" + Arrays.toString(termAggregations))
                .add("dateHistogramAggregation=" + dateHistogramAggregation)
                .toString();
    }
}
