package cc.sofast.infrastructure.customization;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.util.List;
import java.util.Map;

/**
 * @author wxl
 * 获取每个租户的定制化属性
 */
public class TenantCustomization {

    private final CustomizationLoader customizationLoader;

    private final ObjectMapper objectMapper;

    public TenantCustomization(CustomizationLoader customizationLoader) {
        this.customizationLoader = customizationLoader;
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        //TODO 一些通用的配置
        this.objectMapper = builder.build();
    }

    public <T> List<T> getList(TKey key, Class<T> type) {
        String val = customizationLoader.val(key);
        CollectionLikeType collectionLikeType = objectMapper.getTypeFactory().constructCollectionLikeType(List.class, type);
        try {
            return objectMapper.readValue(val, collectionLikeType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> Map<String, T> getMap(TKey key, Class<T> valType) {
        String val = customizationLoader.val(key);
        MapLikeType mapLikeType = objectMapper.getTypeFactory().constructMapLikeType(Map.class, String.class, valType);
        try {
            return objectMapper.readValue(val, mapLikeType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public <K, V> Map<K, V> getMap(TKey key, Class<K> keyClass, Class<V> valType) {
        String val = customizationLoader.val(key);
        MapLikeType mapLikeType = objectMapper.getTypeFactory().constructMapLikeType(Map.class, keyClass, valType);
        try {
            return objectMapper.readValue(val, mapLikeType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T getVal(TKey key, Class<T> type) {
        String val = customizationLoader.val(key);
        JavaType javaType = objectMapper.getTypeFactory().constructType(type);
        try {
            return objectMapper.readValue(val, javaType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String getVal(TKey key) {
        String val = customizationLoader.val(key);
        JavaType javaType = objectMapper.getTypeFactory().constructType(String.class);
        try {
            return objectMapper.readValue(val, javaType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean saveOrUpdate(TKey key, Object val) {
        String valJson;
        try {
            valJson = objectMapper.writeValueAsString(val);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return customizationLoader.saveOrUpdate(key,valJson);
    }

    public boolean remove(TKey key) {

        return customizationLoader.remove(key);
    }
}
