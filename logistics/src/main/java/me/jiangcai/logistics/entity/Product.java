package me.jiangcai.logistics.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * 货品
 *
 * @author CJ
 */
@Setter
@Getter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Product {

    /**
     * 含义上跟enable完全不同；该值为true 则该货品不会在系统中可见！
     */
    private boolean deleted = false;
    /**
     * 可用状态
     */
    private boolean enable = true;
    @Column(columnDefinition = "timestamp")
    private LocalDateTime createTime = LocalDateTime.now();
    /**
     * 是否需要安装
     */
    private boolean installation;
    /**
     * 货物编码|产品编码
     */
    @Id
    @Column(length = 20)
    private String code;
    /**
     * 产品名称
     */
    @Column(length = 40)
    private String name;
    /**
     * 品牌
     */
    @Column(length = 100)
    private String brand;

    /**
     * 主类目
     */
    @Column(length = 100)
    private String mainCategory;

    /**
     * 简易描述
     */
    @Lob
    private String description;
    /**
     * 富文本描述
     */
    @Lob
    private String richDescription;

    @Column(length = 80)
    private String SKU;
    @Column(length = 10)
    private String unit;

    /**
     * 长度，单位mm
     */
    @Column(scale = 2, precision = 12)
    private BigDecimal volumeLength;
    /**
     * 宽度，单位mm
     */
    @Column(scale = 2, precision = 12)
    private BigDecimal volumeWidth;
    /**
     * 高度，单位mm
     */
    @Column(scale = 2, precision = 12)
    private BigDecimal volumeHeight;
    /**
     * 重量，单位g
     */
    @Column(scale = 2, precision = 12)
    private BigDecimal weight;
    /**
     * todo 这里应该将主图压缩成小图和中图
     * 货品主图资源路径
     * {@link me.jiangcai.lib.resource.service.ResourceService#getResource(String)}
     */
    @Column(length = 60)
    private String mainImg;
    /**
     * 货品类型
     */
    @ManyToOne
    private ProductType productType;
    /**
     * 属性值
     */
    @ElementCollection
    private Map<PropertyName, String> propertyNameValues;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return Objects.equals(code, product.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return "Product{" +
                "enable=" + enable +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", SKU='" + SKU + '\'' +
                ", productType=" + productType +
                '}';
    }

    /**
     * @return 体积，单位mm'3
     */
    public BigDecimal getVolume() {
        return getVolumeLength().multiply(getVolumeWidth()).multiply(getVolumeHeight());
    }

    private Map<PropertyName, String> getPropertyNameValuesBySpec(boolean spec) {
        Map<PropertyName, String> propertyNameStringMap = new TreeMap<>(
                (o1, o2) -> o2.getWeight() - o1.getWeight());
        propertyNameValues.keySet().stream().filter(p -> p.isSpec() == spec)
                .forEach(p -> propertyNameStringMap.put(p, propertyNameValues.get(p)));
        return propertyNameStringMap;
    }

    public Map<PropertyName, String> getSpecPropertyNameValues() {
        return getPropertyNameValuesBySpec(true);
    }

    public Map<PropertyName, String> getNoSpecPropertyNameValues() {
        return getPropertyNameValuesBySpec(false);
    }
}
