package cc.sofast.infrastructure.customization;


import cc.sofast.infrastructure.customization.notify.TenantCustomizationEventChanger;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author wxl
 * 获取每个租户的定制化属性
 */
public class TenantCustomization {

    private final CustomizationLoader customizationLoader;

    private final ObjectMapper objectMapper;

    private final TenantCustomizationEventChanger tenantCustomizationEventChanger;

    public TenantCustomization(CustomizationLoader customizationLoader, TenantCustomizationEventChanger tenantCustomizationEventChanger) {
        this.customizationLoader = customizationLoader;
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.serializationInclusion(JsonInclude.Include.NON_NULL);
        this.objectMapper = builder.build();
        this.tenantCustomizationEventChanger = tenantCustomizationEventChanger;
    }

    public <T> List<T> getList(TKey key, Class<T> type) {
        String val = customizationLoader.val(key);
        if (!StringUtils.hasText(val)) {
            return null;
        }
        CollectionLikeType collectionLikeType = objectMapper.getTypeFactory().constructCollectionLikeType(List.class, type);
        try {
            return objectMapper.readValue(val, collectionLikeType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> Map<String, T> getMap(TKey key, Class<T> valType) {
        String val = customizationLoader.val(key);
        if (!StringUtils.hasText(val)) {
            return null;
        }
        MapLikeType mapLikeType = objectMapper.getTypeFactory().constructMapLikeType(Map.class, String.class, valType);
        try {
            return objectMapper.readValue(val, mapLikeType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public <K, V> Map<K, V> getMap(TKey key, Class<K> keyClass, Class<V> valType) {
        String val = customizationLoader.val(key);
        if (!StringUtils.hasText(val)) {
            return null;
        }
        MapLikeType mapLikeType = objectMapper.getTypeFactory().constructMapLikeType(Map.class, keyClass, valType);
        try {
            return objectMapper.readValue(val, mapLikeType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T getVal(TKey key, Class<T> type) {
        String val = customizationLoader.val(key);
        if (!StringUtils.hasText(val)) {
            return null;
        }
        JavaType javaType = objectMapper.getTypeFactory().constructType(type);
        try {
            return objectMapper.readValue(val, javaType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String getVal(TKey key) {
        String val = customizationLoader.val(key);
        if (!StringUtils.hasText(val)) {
            return null;
        }
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
        boolean saveOrUpdate = customizationLoader.saveOrUpdate(key, valJson);
        tenantCustomizationEventChanger.updateOrDelete(key.getTenant(), key.getKey());
        return saveOrUpdate;
    }

    public boolean remove(TKey key) {
        boolean remove = customizationLoader.remove(key);
        tenantCustomizationEventChanger.updateOrDelete(key.getTenant(), key.getKey());
        return remove;
    }
}
